package be.kdg.security.api;

import be.kdg.security.api.dtos.SecuredMessageDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secured")
public class SecuredController {


    @GetMapping("message")
    public SecuredMessageDto getMessage(){
        return SecuredMessageDto.builder()
                .message("I'm a secured message")
                .build();
    }

    @GetMapping("principal")
    public SecuredMessageDto getSecuredWithPrincipal(){
        Jwt token = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getInfoFromToken(token,"I got the information via the current security context");
    }

    @GetMapping("annotation")
    public SecuredMessageDto getSecuredWithPrincipal(@AuthenticationPrincipal Jwt token){
        // Here can you use the subject id
        // (StandardClaimNames.SUB) to create an owner or link the owner (as an external ref)
        // or fetch the owner (in the usecase or service) using the subject id as external ref.

        return getInfoFromToken(token, "I got the information via an annotation");
    }

    @GetMapping("user")
    @PreAuthorize("hasAuthority('user')")
    public SecuredMessageDto getSecuredWithUserRole(@AuthenticationPrincipal Jwt token){
        return getInfoFromToken(token, "I'm a user");
    }


    @GetMapping("owner")
    @PreAuthorize("hasAuthority('owner')")
    public SecuredMessageDto getSecuredWithOwnerRole(@AuthenticationPrincipal Jwt token){
        return getInfoFromToken(token, "I'm an owner");
    }

    private static SecuredMessageDto getInfoFromToken(Jwt token, String message){
        return SecuredMessageDto.builder()
                .subjectid(token.getClaimAsString(StandardClaimNames.SUB))
                .firstName(token.getClaimAsString(StandardClaimNames.GIVEN_NAME))
                .lastName(token.getClaimAsString(StandardClaimNames.FAMILY_NAME))
                .email(token.getClaimAsString(StandardClaimNames.EMAIL))
                .message(message)
                .build();

    }

}
