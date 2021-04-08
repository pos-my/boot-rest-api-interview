package posmy.interview.boot.controller;

import posmy.interview.boot.dto.BaseDto;

import java.security.Principal;
import java.util.Date;

public class BaseController {

    public void setCreatedByAndDate(BaseDto dto, Principal principal) {
        setCreatedByAndDate(dto, principal.getName());
    }

    public void setCreatedByAndDate(BaseDto dto, String loginId) {
        dto.setCreatedBy(loginId);
        dto.setCreatedDate(new Date());
    }

    public void setUpdatedByAndDate(BaseDto dto, Principal principal) {
        dto.setUpdatedBy(principal.getName());
        dto.setUpdatedDate(new Date());
    }

}
