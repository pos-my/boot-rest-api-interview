package posmy.interview.boot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.config.UsersConfig;
import posmy.interview.boot.controllers.dtos.UserReq;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(UsersConfig.class)
public class MemberControllerTest {
    private EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().stringLengthRange(5, 20).build();

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()) // enable security for the mock set up
                .build();
    }

    @WithUserDetails("lib")
    @Test
    public void testList_lib() throws Exception {
        mvc.perform(get("/member/list").contentType(MediaType.APPLICATION_JSON).content("")).andExpect(status().isOk());
    }

    @WithUserDetails("mem")
    @Test
    public void testList_mem() throws Exception {
        mvc.perform(get("/member/list").contentType(MediaType.APPLICATION_JSON).content("")).andExpect(status().isOk());
    }

    @WithUserDetails("lib")
    @Test
    public void testUpd_lib() throws Exception {
        mvc.perform(put("/member/update").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(enhancedRandom.nextObject(UserReq.class))))
                .andExpect(status().is(greaterThan(403)));
    }

    @WithUserDetails("mem")
    @Test
    public void testUpd_mem() throws Exception {
        mvc.perform(put("/member/update").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(enhancedRandom.nextObject(UserReq.class))))
                .andExpect(status().is(greaterThan(403)));
    }

    @WithUserDetails("lib")
    @Test
    public void testAdd_lib() throws Exception {
        mvc.perform(post("/member/add").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enhancedRandom.nextObject(UserReq.class))))
                .andExpect(status().isOk());
    }

    @WithUserDetails("mem")
    @Test
    public void testAdd_mem() throws Exception {
        mvc.perform(post("/member/add").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enhancedRandom.nextObject(UserReq.class))))
                .andExpect(status().is(greaterThan(403)));
    }

    @WithUserDetails("lib")
    @Test
    public void testRet_lib() throws Exception {
        mvc.perform(post("/member/return/{memberName}", enhancedRandom.nextObject(String.class)).contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().is(greaterThan(403)));
    }

    @WithUserDetails("mem")
    @Test
    public void testRet_mem() throws Exception {
        mvc.perform(post("/member/return/{memberName}", enhancedRandom.nextObject(String.class)).contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().is(greaterThan(403)));
    }

    @WithUserDetails("lib")
    @Test
    public void testBor_lib() throws Exception {
        mvc.perform(post("/member/borrow/{memberName}", enhancedRandom.nextObject(String.class)).contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().is(greaterThan(403)));
    }

    @WithUserDetails("mem")
    @Test
    public void testBor_mem() throws Exception {
        mvc.perform(post("/member/borrow/{memberName}", enhancedRandom.nextObject(String.class)).contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().is(greaterThan(403)));
    }

    @WithUserDetails("lib")
    @Test
    public void testRem_lib() throws Exception {
        mvc.perform(delete("/member/remove/{memberName}", enhancedRandom.nextObject(String.class)).contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk());
    }

    @WithUserDetails("mem")
    @Test
    public void testRem_mem() throws Exception {
        mvc.perform(delete("/member/remove/{memberName}", enhancedRandom.nextObject(String.class)).contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk());
    }
}
