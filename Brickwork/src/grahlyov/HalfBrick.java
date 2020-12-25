package grahlyov;

/*
A class for a half of a brick. Each WholeBrick has to contain exactly 2 object of class HalfBrick.
Used for getting the coordinates of a HalfBrick, its distance and orientation to another HalfBrick.
 */

public class HalfBrick {

    private int x;                  // A variable for the x coordinate
    private int y;                  // A variable for the y coordinate

    public HalfBrick(int x, int y) {            // A constructor for the class
        this.x = x;
        this.y = y;
    }

    public int getX() {             // A getter for the x variable
        return x;
    }

    public int getY() {             // A getter for the y variable
        return y;
    }

    public double distance (HalfBrick otherHalf) {              // A method for finding the distance between two halves
        return Math.sqrt(Math.pow(this.x-otherHalf.getX(), 2) + Math.pow(this.y-otherHalf.getY(), 2));
    }

    public boolean isVertical (HalfBrick otherHalf) {           // A method for finding the orientation of two bricks
        return ((this.y - otherHalf.getY()) != 0);
    }
}
