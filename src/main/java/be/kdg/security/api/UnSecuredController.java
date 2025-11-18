package be.kdg.security.api;

import be.kdg.security.api.dtos.UnsecuredMessageDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unsecured")
public class UnSecuredController {


    @GetMapping
    public UnsecuredMessageDto getUnsecured(){
        return new UnsecuredMessageDto();
    }


}
