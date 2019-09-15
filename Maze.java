import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.*;
import java.io.*;

public class Maze {

    
    
    public static class Node {
        private int x;
        private int y;


        Node(int x, int y){
            this.x = x;
            this.y = y;
        }


        int getX(){
            return this.x;
        }

        int getY(){
            return this.y;
        }

//        @Override
//        public int hashCode(){
//            return this.getX()+this.getY()+3;
//        }

/*        @Override
        public boolean equals(Object obj){
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false; 
            Node tmp = (Node) obj; 
            return tmp.getX() == this.getX() && this.getY() == tmp.getY();
        }

        @Override 
        public String toString(){
            return "x: " + this.getX() + " y: " + this.getY();
        }*/
    }
    private int[][] maze;
    // previous grids array
    private Node[][] prev;

    private int sizeX;
    private int sizeY;

    private Node lastNode;

    Maze(int[][] maze, int sizeY, int sizeX) {
        this.maze = maze;
        this.sizeY = sizeY;
        this.sizeX = sizeX;

        prev = new Node[sizeY][sizeX];
    }

    //Keep the path inside the maze;
    private boolean inBoundsX(int number){
        return number >= 0 && number < sizeX;
    }
    private boolean inBoundsY(int number){
        return number >= 0 && number < sizeY;
    }

    //find path from a "start" point. 
    public void solve(Node start){
        Stack<Node> stack = new Stack<>();
        HashSet<Node> visited = new HashSet<>();

        stack.push(start);

        while(!stack.isEmpty()) {
            Node tmp = stack.pop();
            visited.add(tmp);

            if (maze[tmp.getY()][tmp.getX()] == 3) {
                lastNode = tmp;
                break;
            }

            for(Node node : this.getAdjacentEdges(tmp)) {
                if (!visited.contains(node)) {
                    stack.push(node);
                    prev[node.getY()][node.getX()] = tmp;
                }
            }
        }
    }

    //print out the path. 
    public void fillPath() {
        if (lastNode == null) {
            System.out.println("No path in maze");
        } else {
            // assume, that start point and end point are different
            for (;;) {
                lastNode = prev[lastNode.getY()][lastNode.getX()];

                // There's no previous node for start point, so we can break
                if (lastNode == null) {
                    break;
                }

                maze[lastNode.getY()][lastNode.getX()] = 9;

            }
        }
    }

    private List<Node> getAdjacentEdges(Node tmp) {
        List<Node> neighbours = new ArrayList<Node>();
        if(this.inBoundsX(tmp.getX()+1)){
            if(this.maze[tmp.getY()][tmp.getX()+1] != 1){
                neighbours.add(new Node(tmp.getX()+1, tmp.getY()));
            }
        }
        if(this.inBoundsX(tmp.getX()-1)){
            if(this.maze[tmp.getY()][tmp.getX()-1] != 1){
                neighbours.add(new Node(tmp.getX()-1, tmp.getY()));
            }
        }
        if(this.inBoundsY(tmp.getY()+1)){
            if(this.maze[tmp.getY()+1][tmp.getX()] != 1){
                neighbours.add(new Node(tmp.getX(), tmp.getY()+1));
            }
        }
        if(this.inBoundsY(tmp.getY()-1)){
            if(this.maze[tmp.getY()-1][tmp.getX()] != 1){
                neighbours.add(new Node(tmp.getX(), tmp.getY()-1));
            }
        }
        return neighbours;
    }


    public static void main(String args[]){
        int [][] maze =
                {   {1,1,1,1,1,1,1,1,1,1,1,1,1},
                    {1,0,1,0,1,0,1,0,0,0,0,0,1},
                    {1,0,1,0,0,0,1,0,1,1,1,0,1},
                    {1,0,0,0,1,1,1,0,0,0,0,0,1},
                    {1,0,1,0,0,3,0,0,1,1,1,0,1},
                    {1,0,1,0,1,1,1,0,1,0,0,0,1},
                    {1,0,1,0,1,0,0,0,1,1,1,0,1},
                    {1,0,1,0,1,1,1,0,1,0,1,0,1},
                    {1,0,0,0,0,0,0,0,0,0,1,0,1},
                    {1,1,1,1,1,1,1,1,1,1,1,1,1}

                };

        // Create maze with certain dimensions
        Maze m = new Maze(maze, 10, 13);

        m.solve(new Node(1,1));

        m.fillPath();

        for(int i = 0; i < maze.length; i++){
            for(int j = 0; j < maze[i].length; j++){
                System.out.print(" " + maze[i][j] + " ");
            }
            System.out.println();
        }
    }
}