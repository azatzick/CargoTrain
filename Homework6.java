/*******************************************************************************
Alina Zatzick
October 28th 2020
CS201 - HW6 Doubly Linked Lists

This assignment uses linked lists to load and unload a theoretical train, sorting
objects into cities and cargo Types along the way. Additionally, each objecgt is
assigned an identification number to individualize it. The objects are loaded
from the file Cargo.txt, and unloaded at the cities in the file Destinations.txt.
At the end of the program, the remaining items on the train are printed
********************************************************************************/
import java.io.File;
import java.util.Scanner;

class FreightCar{
  int id;
  String cargoType;
  String destination;

  FreightCar(int identification, String cargT, String dest){
    id = identification;
    cargoType = cargT;
    destination = dest;
  }
  public String toString(){
    return "00" + id + " " + destination + " " + cargoType;
  }
}

class LinkedList{
  Scanner scan;
  //attributes
  Node head;
  Node tail;

  // constructor for linked list
  public LinkedList(){
    head = null;
    tail = null;
  }
  class Node{
    // attributes
    FreightCar data;
    Node next;
    // previous for double linked list
    Node prev;

    // constructor for node
    public Node(FreightCar freight){
      prev = null;
      next = null;
      data = freight;
    }
  }
  // adds freight object to doubly linked list
  // under conditions of destination and cargo type
  public void add(FreightCar freight){
    Node newnode = new Node(freight);
    Node curr = head;

    // list is empty
    if (curr == null){
      head = newnode;
      tail = newnode;
      tail.next = null;
    }

    //initializing previous node
    Node prev = head.prev;

    //list is not empty

    // destination is not yet found
    while(curr != null && !curr.data.destination.equals(freight.destination)){
        prev = curr;
        curr = curr.next;
    }
    // destination has been found, but cargo type has not
    while(curr != null && curr.data.destination.equals(freight.destination) && !curr.data.cargoType.equals(freight.cargoType)){
    prev = curr;
    curr = curr.next;
  }

    // not at end of list
    if (curr != null){

      //general add case
      if(curr.data.destination.equals(freight.destination) && curr.data.cargoType.equals(freight.cargoType)){
        newnode.next = curr.next;
        newnode.prev= curr;
        curr.next = newnode;
        curr.next.prev = newnode;
      }
      // cargo type could not be found in destination
      // adding new cargo type to the destination
      else if(!curr.data.destination.equals(freight.destination)){
        newnode.next = curr;
        newnode.prev = prev;
        curr.prev = newnode;
        prev.next = newnode;
      }

    }
    // reached end of list without finding destination -- want to add to end of list
    else if (curr == null){
      newnode.prev= tail;
      tail.next = newnode;

      tail = newnode;
      tail.next = null;
    }
  }

  // removing freight objects from the linked list
  public void remove (Node n, String destination){

    Node curr = null;
    Node prev = null;

    // element we want to remove is  first element
    if (head != null && head.data.destination.equals(destination)){
      System.out.println(head.data);

      //one element in list
      if (head == tail){
        head = null;
        tail = null;
      }

      // multiple elements in list
      else{
        head = head.next;
        head.prev = null;

      }
    }
    // could be in list, but not the first element
    else{

      curr = head.next;
      prev = head;

      // continue until found or reach end of list
      while(curr != null && !curr.data.destination.equals(destination)){
        prev = curr;
        curr = curr.next;
      }
      // we have not reached the end
      // we found the item
      if (curr != null){
        System.out.println(curr.data);

        // if val is last element in list
        if(curr == tail){
          tail = prev;
          tail.next = null;
        }
        // val is not last element
        // general case
        else{
        prev.next = curr.next;
        curr.next.prev = prev;
        }
      }
    }
  }

  //prints doubly linked list
  public void printList(){
    Node curr = head;

    while(curr != null){
      System.out.println(curr.data);
      curr = curr.next;
    }
  }
  // reading destination file and abstracting String data
  // parsing through linked list
  // removing every freight object with this destination
  public LinkedList readDestinations(String filename, LinkedList linked){
    // try - catch statement
    try{
      File file = new File(filename);
      scan = new Scanner(file);
    }
    catch(Exception e){
      System.out.println("File not found.");
    }

    // creating String array to store file data
    while(scan.hasNextLine()){
      String line = scan.nextLine();
      String [] ss = line.split("\n");
      Node curr = head;

      // iterating through each element in the string
      for(int i = 0; i < ss.length; i ++){
        System.out.println("Now arriving at " + ss[i] +". " + "Unloading cargo." );

        //iterating through linked lists
        //removing node with destination ss[i]

        while(curr != null){
          linked.remove(curr,ss[i]);
          curr = curr.next;
          }
          System.out.println();
        }
      }
      return linked;
    }
}

public class Homework6{
  static Scanner scan;

  // reads and obtains cargo information from file, Cargo.txt
  static LinkedList readCargo(String filename){
    int id = 0;

    // try - catch statement
    try{
      File file = new File(filename);
      scan = new Scanner(file);
    }
    catch(Exception e){
      System.out.println("File not found.");
    }

    //instance of linked list
    LinkedList linked = new LinkedList();

    //reading file
    while (scan.hasNextLine()){
      String line = scan.nextLine();
      String [] ss = line.split(",");
      FreightCar freight = new FreightCar(id, ss[1], ss[0]);
      id ++;

      //adding data to list
      linked.add(freight);
    }
    return linked;
  }

  public static void main(String args[]){

    //reading cargo file ile
    //adding freight cars to linked list
    LinkedList link = readCargo(args[0]);

    //printing sorted data
    System.out.println("\n-- CargoManifest --\n");
    link.printList();
    System.out.println();

    //reading destination file
    //removing freight cars with these destinations
    LinkedList remainingLink = link.readDestinations(args[1], link);

    //printing remainder of linked list
    System.out.println("What's left on the train?");
    remainingLink.printList();
  }
}
