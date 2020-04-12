package com.narendra;

import java.util.ArrayList;
import java.util.Scanner;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

public class SpringSecurityCoreCustomExampleApplication {
	
	private static AuthenticationManager authenticationManager = new AuthenticationManagerImpl();

	public static void main(String[] args) {
		String userNm = null;
		String password = null;
		try(Scanner scanner = new Scanner(System.in)){ // Here the Java 7 feature try with resources is implemented to close the resource (scanner) automatically without finally block
			System.out.println("Enter User Name :");
			userNm = scanner.nextLine();
			System.out.println("Enter Password :");
			password = scanner.nextLine();
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(userNm, password);
		System.out.println("authentication "+authentication);
		Authentication authenticationResult  = authenticationManager.authenticate(authentication);
		System.out.println("authenticationResult "+authenticationResult);
		
	}
}

	class AuthenticationManagerImpl implements AuthenticationManager{

		public UserDetailsService userDetailsService() {
			InMemoryUserDetailsManager inMemoryUserDetails = new InMemoryUserDetailsManager();
			inMemoryUserDetails.createUser(User.withUsername("narendra").password("reddy").roles("ADMIN").build());
			inMemoryUserDetails.createUser(User.withUsername("uma").password("yama").roles("USER").build());
			return inMemoryUserDetails;
		}

		@Override
		public Authentication authenticate(Authentication authentication) throws AuthenticationException {
			
			UserDetails userDetails = userDetailsService().loadUserByUsername(authentication.getName());
			if(userDetails.getUsername().equalsIgnoreCase("narendra") &&
					userDetails.getPassword().equalsIgnoreCase(authentication.getCredentials().toString())) {
				return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), 
						new ArrayList<GrantedAuthority>() {
					{
						add(new SimpleGrantedAuthority("ROLE_ADMIN"));
					}
						});
			}
			
			if(userDetails.getUsername().equalsIgnoreCase("uma") &&
					userDetails.getPassword().equalsIgnoreCase(authentication.getCredentials().toString())) {
				return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), 
						new ArrayList<GrantedAuthority>() {
					{
						add(new SimpleGrantedAuthority("ROLE_USER"));
					}
						});
			}
			
			throw new BadCredentialsException("Bad credentails");
		}
		
	}
	
