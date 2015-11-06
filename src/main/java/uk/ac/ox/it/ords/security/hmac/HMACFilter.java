package uk.ac.ox.it.ords.security.hmac;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;

import uk.ac.ox.it.ords.security.configuration.HmacUserList;

public class HMACFilter extends AuthenticatingFilter{
	
	

	@Override
	protected AuthenticationToken createToken(ServletRequest request,
			ServletResponse response) throws Exception {
		if (Hmac.isValidSignedRequest((HttpServletRequest) request, HmacUserList.getHmacUsers())){
			HMACToken token = new HMACToken();
			token.setPrincipal(Hmac.getPublicKey((HttpServletRequest) request));
			token.setCredentials(Hmac.getSignature((HttpServletRequest) request));		

			return token;
		} else {
			return null;
		}
	}
	
	

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		return false;
	}

}
