package com.skillswap.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for when a skill with a duplicate name is created.
 * The @ResponseStatus annotation ensures Spring automatically returns
 * a 409 CONFLICT status code when this exception is thrown.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateSkillException extends RuntimeException {

    public DuplicateSkillException(String message) {
        super(message);
    }
}


///**
//* Custom exception for when a requested skill is not found.
//* The @ResponseStatus annotation ensures Spring automatically returns
//* a 404 NOT_FOUND status code when this exception is thrown.
//*/
//@ResponseStatus(HttpStatus.NOT_FOUND)
//public class SkillNotFoundException extends RuntimeException {
//
// public SkillNotFoundException(String message) {
//     super(message);
// }
//}
