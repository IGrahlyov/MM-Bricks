package grahlyov;

/*
A class representing the whole brick, used for checking if the entered input is valid,
the orientation of the brick and whether we've "passed" the first half of it already
 */

import java.util.ArrayList;

public class WholeBrick {

    private ArrayList<HalfBrick> halfBricks;                // An array list of the brick's halves
    private boolean isPassed = false;                       // A variable to check if we've "passed" the first half of the brick


    public WholeBrick() {                                   // A constructor for the class
        this.halfBricks = new ArrayList<>();
    }

    public WholeBrick addHalfBrick (HalfBrick halfBrick) {  // A method for adding halves in the ArrayList
        this.halfBricks.add(halfBrick);
        return this;
    }

    public ArrayList<HalfBrick> getHalfBricks() {           // A getter for the ArrayList
        return halfBricks;
    }

    public void setPassed(boolean passed) {                 // A setter for the isPassed variable
        isPassed = passed;
    }

    public boolean isPassed() {                             // A getter for the isPassed variable
        return isPassed;
    }

    public boolean isVertical () {                          // A method used to check whether the brick is vertical or horizontal
        return this.halfBricks.get(0).isVertical(this.halfBricks.get(1));
    }
}
