import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class CirclesTest {

    public static void main(String[] args) throws ObjectCanNotBeMovedException {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try {
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try {
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());


    }
}


interface Movable{
    void moveUp() throws ObjectCanNotBeMovedException;
    void moveLeft() throws ObjectCanNotBeMovedException;
    void moveRight() throws ObjectCanNotBeMovedException;
    void moveDown() throws ObjectCanNotBeMovedException;
    int getCurrentXPosition();
    int getCurrentYPosition();
    TYPE getType();

}

class ObjectCanNotBeMovedException extends Exception{
    public ObjectCanNotBeMovedException(String message) {
        super(message);
    }
}

class MovablePoint implements Movable {
    int x;
    int y;
    int xSpeed;
    int ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if(y + ySpeed > MovablesCollection.y_MAX)
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds", x, y+ySpeed));
        y += ySpeed;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if(y - ySpeed < 0)
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds", x, y-ySpeed));
        y -= ySpeed;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if(x + xSpeed > MovablesCollection.x_MAX)
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds", x+xSpeed, y));
        x += xSpeed;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if(x - xSpeed < 0)
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds", x-xSpeed, y));
        x -= xSpeed;
    }

    private void pleaseMove(int coordinate, int speed) throws ObjectCanNotBeMovedException{
        if(coordinate+speed>MovablesCollection.x_MAX || coordinate+speed>MovablesCollection.y_MAX || coordinate+speed<0)
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds", x, y+ySpeed));
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }
    @Override
    public TYPE getType(){
        return TYPE.POINT;
    }

    @Override
    public String toString() {
        return "Movable point with coordinates ("+x+","+y+")";
    }
}

class MovableCircle implements Movable{
    int radius;
    MovablePoint center;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException{
        center.moveUp();
    }

    @Override
    public void moveLeft()throws ObjectCanNotBeMovedException {
        center.moveLeft();
    }

    @Override
    public void moveRight()throws ObjectCanNotBeMovedException {
        center.moveRight();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException{
        center.moveDown();
    }
    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }
    @Override
    public TYPE getType(){
        return TYPE.CIRCLE;
    }

    public String toString() {
        return String.format("Movable circle with center coordinates (%d,%d) and radius %d", getCurrentXPosition(), getCurrentYPosition(), radius);
    }

    public int getRadius() {
        return radius;
    }
}

class MovableObjectNotFittableException extends Exception{
    public MovableObjectNotFittableException(String message) {
        super(message);
    }
}

class MovablesCollection {
    Movable[] movables;
    static int x_MAX=0;
    static int y_MAX=0;

    public MovablesCollection(Movable[] movables) {
        this.movables = new Movable[0];
    }

    public MovablesCollection (int x, int y){
        movables = new Movable[0];
        x_MAX = x;
        y_MAX = y;
    }

    public static void setxMax(int x_MAX) {
        MovablesCollection.x_MAX = x_MAX;
    }

    public static void setyMax(int y_MAX) {
        MovablesCollection.y_MAX = y_MAX;
    }

    void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        if (m.getCurrentXPosition() >= 0&&m.getCurrentXPosition() < MovablesCollection.x_MAX&&m.getCurrentYPosition() >= 0 && m.getCurrentYPosition() < y_MAX) {

            if(m instanceof MovableCircle)
            {
                MovableCircle temp = (MovableCircle) m;
                if(temp.getCurrentXPosition() + temp.getRadius() > x_MAX || temp.getCurrentXPosition() - temp.getRadius() < 0
                        || temp.getCurrentYPosition() + temp.getRadius() > y_MAX || temp.getCurrentYPosition() - temp.getRadius() < 0) {
                    throw new MovableObjectNotFittableException(String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection", temp.getCurrentXPosition(), temp.getCurrentYPosition(), temp.getRadius()));
                }
            }
                Movable[] newMovable = new Movable[movables.length + 1];
                System.arraycopy(movables, 0, newMovable, 0, movables.length);
                newMovable[movables.length] = m;
                movables = newMovable;
        }
        else
        {
            throw new MovableObjectNotFittableException(m.toString() + " can not be fitted into the collection");
        }
    }

    void moveObjectsFromTypeWithDirection (TYPE type, DIRECTION direction) throws ObjectCanNotBeMovedException {
        for (int i=0; i<movables.length; i++){
            try {
                if (movables[i].getType() == type) {
                    if (direction == DIRECTION.UP)
                        movables[i].moveUp();
                    else if (direction == DIRECTION.DOWN)
                        movables[i].moveDown();
                    else if (direction == DIRECTION.LEFT)
                        movables[i].moveLeft();
                    else if (direction == DIRECTION.RIGHT)
                        movables[i].moveRight();
                }
            }
            catch (ObjectCanNotBeMovedException e){
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public String toString() {
        String str = "Collection of movable objects with size " + movables.length + ":\n";
        for(Movable mv : movables){
            str+=mv.toString()+'\n';
        }
        return str;
    }
}