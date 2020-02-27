package com.sociallogin;


/**
 * @author Manish Kumar
 * @since 15/9/17
 */


public interface SocialLoginListener {


    void socialLoginSuccess(SocialData socialData);

    void fbLoginPermissionError();

}
