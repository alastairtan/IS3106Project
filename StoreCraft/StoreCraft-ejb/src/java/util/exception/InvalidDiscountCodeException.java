package util.exception;



public class InvalidDiscountCodeException extends Exception
{
    public InvalidDiscountCodeException()
    {
    }
    
    
    
    public InvalidDiscountCodeException(String msg)
    {
        super(msg);
    }
}