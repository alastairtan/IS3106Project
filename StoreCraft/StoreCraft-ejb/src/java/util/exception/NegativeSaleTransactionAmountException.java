package util.exception;



public class NegativeSaleTransactionAmountException extends Exception
{
    public NegativeSaleTransactionAmountException()
    {
    }
    
    
    
    public NegativeSaleTransactionAmountException(String msg)
    {
        super(msg);
    }
}