package org.spring.core.learn.spring14fitclubmvc.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.spring.core.learn.spring14fitclubmvc.dto.request.LoginRequest;
import org.spring.core.learn.spring14fitclubmvc.dto.request.MemberRegistrationRequest;
import org.spring.core.learn.spring14fitclubmvc.dto.response.MemberResponse;
import org.spring.core.learn.spring14fitclubmvc.entity.Member;
import org.spring.core.learn.spring14fitclubmvc.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/*
 * ================= REST API: @RestController =================
 * @RestController = @Controller + @ResponseBody on EVERY method.
 * @ResponseBody tells Spring "run the return value through an
 * HttpMessageConverter (Jackson) and write it as JSON" - the same
 * mechanism as @RequestBody, running in the opposite direction.
 * There is no more ModelAndView, no view names - every method returns

 * data directly. React is responsible for rendering forms, showing
 * validation errors, and redirecting after login.
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }



    /*
     * POST /api/auth/register
     * Body: { "username": "...", "email": "...", "password": "..." }
     * @Valid triggers Bean Validation; on failure Spring throws
     * MethodArgumentNotValidException BEFORE this method runs - caught
     * globally by GlobalExceptionHandler (400 + field errors).
     * ResponseEntity.status(CREATED) -> HTTP 201, correct for "a new
     * resource was created" (no browser redirect makes sense for JSON).
     */
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> register(@Valid @RequestBody MemberRegistrationRequest request)
    {
        Member registered = memberService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(MemberResponse.from(registered));
    }


    /*
     * POST /api/auth/login
     * Still uses HttpSession - Spring Boot sets a JSESSIONID cookie
     * automatically. React needs `credentials: 'include'` (fetch) or
     * `withCredentials: true` (axios) for this cookie to be sent/stored
     * cross-origin.
     */
    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(@Valid @RequestBody LoginRequest request , HttpSession session){
        Member member = memberService.login(request.getUsername(), request.getPassword() );
        session.setAttribute("loggedInMemberId", member.getId());
        return ResponseEntity.ok(MemberResponse.from(member));
    }



    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session){
        session.invalidate();
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/me")
    public ResponseEntity<Long> currentMember(HttpSession session)
    {
        Object memberId = session.getAttribute("loggedInMemberId");
        if(memberId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok((Long) memberId);
    }







}