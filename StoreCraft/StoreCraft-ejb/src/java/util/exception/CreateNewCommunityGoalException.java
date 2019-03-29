/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author bryan
 */
public class CreateNewCommunityGoalException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewCommunityGoalException</code>
     * without detail message.
     */
    public CreateNewCommunityGoalException() {
    }

    /**
     * Constructs an instance of <code>CreateNewCommunityGoalException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewCommunityGoalException(String msg) {
        super(msg);
    }
}
