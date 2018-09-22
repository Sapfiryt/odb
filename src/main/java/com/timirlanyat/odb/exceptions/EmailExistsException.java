package com.timirlanyat.odb.exceptions;

public class EmailExistsException extends Exception {


    public EmailExistsException(){}

    public EmailExistsException(String email){
        super( "There is an account with that email adress: "
                + email);

    }
}
