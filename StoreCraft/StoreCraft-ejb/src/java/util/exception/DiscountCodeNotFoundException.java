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
public class DiscountCodeNotFoundException extends Exception{
    public DiscountCodeNotFoundException()
    {
    }
    public DiscountCodeNotFoundException(String msg)
    {
        super(msg);
    }
}
