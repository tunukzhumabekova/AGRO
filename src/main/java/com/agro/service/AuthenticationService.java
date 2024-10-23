package com.agro.service;

import com.agro.model.request.SignIn;
import com.agro.model.request.SignUp;
import com.agro.model.response.Authentication;

public interface AuthenticationService {
   Authentication signUp(SignUp signUp);

   Authentication signIn(SignIn signIn);
}
