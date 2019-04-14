/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Alastair
 */
public class DateNotFoundException extends Exception{

    public DateNotFoundException() {
    }
    
    public DateNotFoundException(String msg) {
        super(msg);
    }
}
