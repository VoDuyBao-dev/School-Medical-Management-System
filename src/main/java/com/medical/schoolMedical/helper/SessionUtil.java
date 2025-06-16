package com.medical.schoolMedical.helper;

import com.medical.schoolMedical.dto.HealthCheckConsentDTO;
import com.medical.schoolMedical.dto.UserDTO;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {
    public static UserDTO getLoggedInUser (HttpSession session){
        return (UserDTO) session.getAttribute("loggedInUser");
    }

    public static Long getLoggedInUserId (HttpSession session){
        UserDTO user =  getLoggedInUser(session);
        return user != null ? user.getId() : null;
    }
}
