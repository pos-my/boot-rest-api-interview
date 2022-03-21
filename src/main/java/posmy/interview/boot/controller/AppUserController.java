package posmy.interview.boot.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import posmy.interview.boot.model.AppUser;
import posmy.interview.boot.model.AppUserRole;
import posmy.interview.boot.model.MessageResponse;
import posmy.interview.boot.service.AppUserService;
import posmy.interview.boot.util.TokenUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequestMapping("/user")
public class AppUserController {
    private final AppUserService userService;
    private final String secret;
    private final Gson gson;

    @Autowired
    public AppUserController(AppUserService userService,
                             @Value("${security.authentication.secret}") String secret,
                             Gson gson) {
        this.userService = userService;
        this.secret = secret;
        this.gson = gson;
    }

    @GetMapping("/get")
    public ResponseEntity<AppUser> getUser(@RequestParam String username){
        return ResponseEntity.ok().body(userService.getUser(username));
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<AppUser>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/save")
    public ResponseEntity<AppUser> saveUser(@RequestBody AppUser user){
        try{
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/save").toUriString());
            return ResponseEntity.created(uri).body(userService.saveUser(user));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/role/save")
    public ResponseEntity<AppUserRole> saveRole(@RequestBody AppUserRole role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUserRole(role));
    }

    @PostMapping("/role/add-to-user")
    public ResponseEntity<?> addRoleToUser(@RequestParam String username,
                                           @RequestParam String roleName){
        try {
            userService.addRoleToUser(username, roleName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(PRECONDITION_FAILED).contentType(APPLICATION_JSON).body(
                    gson.toJson(
                            MessageResponse.builder()
                                    .status(PRECONDITION_FAILED.value())
                                    .message(e.getMessage())
                                    .build()
                    )
            );
        }
    }

    @PostMapping("/role/remove-from-user")
    public ResponseEntity<?> removeRoleFromUser(@RequestParam String username,
                                                @RequestParam String roleName){
        try {
            userService.removeRoleFromUser(username, roleName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(PRECONDITION_FAILED).contentType(APPLICATION_JSON).body(
                    gson.toJson(
                            MessageResponse.builder()
                                    .status(PRECONDITION_FAILED.value())
                                    .message(e.getMessage())
                                    .build()
                    )
            );
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeUser(@RequestParam String username){
        try {
            userService.removeUser(username);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(PRECONDITION_FAILED).contentType(APPLICATION_JSON).body(
                    gson.toJson(
                            MessageResponse.builder()
                                    .status(PRECONDITION_FAILED.value())
                                    .message(e.getMessage())
                                    .build()
                    )
            );
        }
    }

    @DeleteMapping("/self-remove")
    public void selfRemove(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        try {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            DecodedJWT decodedJWT = TokenUtil.verifyToken(authorizationHeader, secret);
            String username = decodedJWT.getSubject();
            userService.removeUser(username);

            Map<String, String> msg = new HashMap<>();
            msg.put("message", "Account removed successfully");

            response.setContentType(APPLICATION_JSON_VALUE);
            response.getOutputStream().write(gson.toJson(msg).getBytes());

        } catch (Exception e){
            log.error("Error while reading token: {}", e.getMessage());

            response.setStatus(PRECONDITION_FAILED.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            response.getOutputStream().write(
                    gson.toJson(
                            MessageResponse.builder()
                                    .status(PRECONDITION_FAILED.value())
                                    .message(e.getMessage())
                                    .build()
                    ).getBytes());
        }
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                DecodedJWT decodedJWT = TokenUtil.verifyToken(refresh_token, secret);
                String username = decodedJWT.getSubject();
                AppUser user = userService.getUser(username);
                String access_token = TokenUtil.generateAccessToken(
                        user.getUsername(),
                        request.getRequestURL().toString(),
                        user.getRoles().stream().map(AppUserRole::getName).toList(),
                        secret
                );

                response.setHeader("access_token", access_token);
                response.setHeader("refresh_token", refresh_token);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);

                response.setContentType(APPLICATION_JSON_VALUE);
                response.getOutputStream().write(gson.toJson(tokens).getBytes());

            } catch (Exception e){
                log.error("Error while reading token: {}", e.getMessage());

                response.setStatus(BAD_REQUEST.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                response.getOutputStream().write(
                        gson.toJson(
                                MessageResponse.builder()
                                        .status(BAD_REQUEST.value())
                                        .message(e.getMessage())
                                        .build()
                        ).getBytes());
            }
        } else {
            log.error("Refresh token is missing");

            response.setStatus(BAD_REQUEST.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            response.getOutputStream().write(
                    gson.toJson(
                            MessageResponse.builder()
                                    .status(BAD_REQUEST.value())
                                    .message("Refresh token is missing")
                                    .build()
                    ).getBytes());
        }
    }

}
