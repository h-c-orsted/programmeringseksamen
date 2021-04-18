import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class main extends PApplet {

Controller ctrl;


Graph tempGraph = new Graph(1150, 20, 600, 400, "Temperatur", 50);
Graph lampGraph = new Graph(50, 200, 400, 200, "Lys tændt/slukket", 30);

Automation automation = new Automation();
Automation tempAutomation = new Automation();
Automation speakerAutomation = new Automation();

float iconSize = 100;


public void setup() {
  

  ctrl = new Controller();

  
  ctrl.addAutomation(automation);
  ctrl.addAutomation(tempAutomation);
  ctrl.addAutomation(speakerAutomation);
  
  ctrl.addGraph(tempGraph);
  ctrl.addGraph(lampGraph);


  ////////////////////////////////////////////////////////////////////////////////////////
  //  Automation 1
  //  Here there is 1 button that can turn on/off (toggle) all the outputs.
  //  The automation will also pass data to a graph and show the current volume.
  ////////////////////////////////////////////////////////////////////////////////////////
  InputButton btn1 = new InputButton(300, 500, iconSize, "Alt on/off");
  LED led = new LED(100, 600, iconSize);
  Lamp lamp = new Lamp(600, 600, iconSize);
  Speaker speaker = new Speaker(900, 600, iconSize);  
  automation.addInput(btn1);
  automation.addOutput(led);
  automation.addOutput(lamp);
  automation.addOutput(speaker);
  automation.addAction(new Action() { 
    public void execute() {
      if (automation.getInput(0) instanceof InputButton) {
        InputButton trigger = (InputButton)automation.getInput(0); 
        // If the trigger is hit, the mouse is pressed and the mouse wasn't pressed at last loop
        if (trigger.isPressed() && mousePressed && mousePressed != trigger.mouseClicked) {
          for (Output o : automation.listO) {
            o.state = !o.state;
          }
          println("Button pressed");
        }

        trigger.mouseClicked = mousePressed;
      } else {
        println("Error: Couldn't get InputButton trigger");
      }
      
      
      // Post state to graph
      Output o = automation.getOutput(0);
      if (frameCount%30==0) {
        float val = (o.state) ? 1 : 0;
        lampGraph.addData(val);
      }
      
      
      // Print the current volume below the speaker
      if (automation.getOutput(2) instanceof Speaker) {
        Speaker s = (Speaker)automation.getOutput(2);
        s.friendlyName = "Vol: " + (int)s.getVolume() + "%";
      }
      
    }
  });


  ////////////////////////////////////////////////////////////////////////////////////////
  //  Automation 2
  //  There is a button which can toggle whether the thermometer is generating
  //  data or not. This will also pass data to a graph.
  ////////////////////////////////////////////////////////////////////////////////////////
  Thermometer thermometer = new Thermometer(1300, 550, iconSize);
  InputButton btn2 = new InputButton(1200, 550, iconSize, "Mål temp.");
  tempAutomation.addInput(thermometer);
  tempAutomation.addInput(btn2);
  tempAutomation.addAction(new Action() {
    public void execute() {
      if (tempAutomation.getInput(1) instanceof InputButton) {
        InputButton trigger = (InputButton)tempAutomation.getInput(1); 
        if (trigger.isPressed() && mousePressed && mousePressed != trigger.mouseClicked) {

          if (tempAutomation.getInput(0) instanceof Thermometer) {
            Thermometer t = (Thermometer)tempAutomation.getInput(0);
            t.toggleGeneratingData();
            println("Toggle thermometer generating data");
          } else {
            println("Error: Couldn't get Thermomter input");
          }
          
          println("Button 2 pressed");
        }
        trigger.mouseClicked = mousePressed;
      } else {
        println("Error: Couldn't get InputButton trigger");
      }


      if (tempAutomation.getInput(0) instanceof Thermometer) {
        Thermometer t = (Thermometer)tempAutomation.getInput(0);
        if (t.isGeneratingData && frameCount%30==0) {
          float val = t.generateData();
          tempGraph.addData(val);
          println(val);
        }
      } else {
        println("Error: Couldn't get Thermomter input");
      }
    }
  });
  
  
  ////////////////////////////////////////////////////////////////////////////////////////
  //  Automation 3
  //  There is 2 buttons - for changing the volume of the speaker either up or down.
  ////////////////////////////////////////////////////////////////////////////////////////
  InputButton volDown = new InputButton(840, 750, iconSize, "Vol -");
  InputButton volUp = new InputButton(960, 750, iconSize, "Vol +");
  speakerAutomation.addInput(volDown);
  speakerAutomation.addInput(volUp);
  speakerAutomation.addOutput(speaker);
  speakerAutomation.addAction(new Action() {
    public void execute() {
      // Turn down volume
      if (speakerAutomation.getInput(0) instanceof InputButton) {
        InputButton trigger = (InputButton)speakerAutomation.getInput(0); 
        // If the trigger is hit, the mouse is pressed and the mouse wasn't pressed at last loop
        if (trigger.isPressed() && mousePressed && mousePressed != trigger.mouseClicked) {
          if (speakerAutomation.getOutput(0) instanceof Speaker) {
            Speaker s = (Speaker)speakerAutomation.getOutput(0);
            s.changeVolume(-10);
          }
        }
        trigger.mouseClicked = mousePressed;
      } else {
        println("Error: Couldn't get InputButton trigger");
      }
      
      // Turn up
      if (speakerAutomation.getInput(1) instanceof InputButton) {
        InputButton trigger = (InputButton)speakerAutomation.getInput(1); 
        // If the trigger is hit, the mouse is pressed and the mouse wasn't pressed at last loop
        if (trigger.isPressed() && mousePressed && mousePressed != trigger.mouseClicked) {
          if (speakerAutomation.getOutput(0) instanceof Speaker) {
            Speaker s = (Speaker)speakerAutomation.getOutput(0);
            s.changeVolume(10);
          }
        }
        trigger.mouseClicked = mousePressed;
      } else {
        println("Error: Couldn't get InputButton trigger");
      }
    }
  });
  
}  // End void setup()



public void draw() {
  clear();
  background(200);
  
  textAlign(LEFT);
  textSize(50);
  text("Stue", 50, 70);
  
  // Display controller and update everything in automations and graphs
  ctrl.update();
}




class Action {
  public void execute() {
  };
}

class MyAction extends Action {
  LED a;
  Speaker b;
  MyAction(LED a, Speaker b) {
    this.a = a;
    this.b = b;
  }
  public void execute() {
    
  }
}

class Automation {
  public ArrayList<Input> listI = new ArrayList<Input>();
  public ArrayList<Output> listO = new ArrayList<Output>();
  public ArrayList<Action> listA = new ArrayList<Action>();
  
  public void addInput(Input i) {
    listI.add(i);
  }
  
  public void addOutput(Output o) {
    listO.add(o);
  }
  
  public void addAction(Action a) {
    listA.add(a);
  }

   
  public Input getInput(int i) {
     return listI.get(i);
  }
  
  public Output getOutput(int i) {
    return listO.get(i);
  }
  
  
  public void update() {
    for (Input i : listI) {i.update();}
    for (Output o : listO) {o.update();}
    for (Action a : listA) {a.execute();}
  }
}

class Controller {
  float aspectRatio = 0.70859375f;
  PImage img = loadImage("arduino.png");
  float x, y;
  float w = 240;
  float h = w * aspectRatio;
  ArrayList<Automation> automationsList = new ArrayList<Automation>();
  ArrayList<Graph> graphsList = new ArrayList<Graph>();
  
  Controller() {
    // Place the controller in center
    x = (1800-w)/2;
    y = (900-h)/2;
  }
  
  Controller(float x, float y, float w) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = w * aspectRatio;
  }
  
  Controller(float x, float y) {
    this.x = x;
    this.y = y;
    this.w = 240;
    this.h = 240 * aspectRatio;
  }
  
  public void display() {image(img, x, y, w, h);}
  
  public void addAutomation(Automation a) {
    automationsList.add(a);
  }
  
  public void addGraph(Graph g) {
    graphsList.add(g);
  }
  
  public void update() {
    display();
    for (Automation a : automationsList) {a.update();}
    for (Graph g : graphsList) {g.show();}
  }
}

class Graph {
  float x, y, w, h;
  float[] data;
  String title;
  
  
  Graph (float x, float y, float w, float h, String title, int countData) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.title = title;
    data = new float[countData];
  }
  
  
  public void addData(float val) {
    // Move every value one step forward
    for (int i=0; i<data.length-1; i++) {
      data[i] = data[i+1];
    }
    // Add the value to the end of the array
    data[data.length-1] = val;
  }
  
  
  public void show() {
    // Setup lines and stuff
    graphLayout();
    displayData();
  }
  
  
  public void graphLayout () {
    textAlign(LEFT);
    stroke(0);
    fill(0);
    rect(x, y, 2, h);
    rect(x, y+h, w, 2);
    textSize(25);
    text(title, x+10, y+20);
  }
  
  
  public void displayData() {
    // Find average to scale the y-axis
    float total = 0, maxVal = 0;
    int countValues = 0;
    for (float val : data) {
      if (val != 0) {
        countValues++;
        total += val;
        if (val > maxVal) maxVal = val;
      }
    }
    float average = total/countValues;
    
    float dataWidth = w/data.length; 
    stroke(60, 80, 230);
    for (int i=0; i<data.length-1; i++) {
      // This value
      float pointX = x + dataWidth*i + 1;
      float pointY = y + h - (h/(maxVal*1.2f)) * data[i];
      pointY = pointY > 0 ? pointY : y+h;
      
      // Next value (for drawing line)
      float point1X = x + dataWidth*(i+1) + 1;
      float point1Y = y + h - (h/(maxVal*1.2f)) * data[i+1];
      point1Y = point1Y > 0 ? point1Y : y+h;
      
      line(pointX, pointY, point1X, point1Y);
    }
  }
  
  
}

class IODevice {
  String friendlyName;
  float aspectRatio = 1;
  PImage img;
  PImage img_active;
  float x, y;
  float w = 240;
  float h;
  boolean activeImgProvided;
  boolean state;
  
  IODevice (String img_src, float x, float y, float w) {
    img = loadImage(img_src);
    this.x = x;
    this.y = y;
    this.w = w;
    h = w * aspectRatio;
  }
  
  IODevice (String img_src, String img_active_src, float x, float y, float w) {
    img = loadImage(img_src);
    img_active = loadImage(img_active_src);
    activeImgProvided = true;
    this.x = x;
    this.y = y;
    this.w = w;
    h = w * aspectRatio;
  }
  
  IODevice (String img_src, float x, float y, float w, String name) {
    img = loadImage(img_src);
    this.x = x;
    this.y = y;
    this.w = w;
    h = w * aspectRatio;
    friendlyName = name;
  }
  
  IODevice (String img_src, String img_active_src, float x, float y, float w, String name) {
    img = loadImage(img_src);
    img_active = loadImage(img_active_src);
    activeImgProvided = true;
    this.x = x;
    this.y = y;
    this.w = w;
    h = w * aspectRatio;
    friendlyName = name;
  }
  
  
  public void update() {
    // If there is an active image and the state is on
    if (activeImgProvided && state) {
      image(img_active, x, y, w, h);
    } else {
      // Is no image is provided or off
      image(img, x, y, w, h);
    }
    
    // Display name below if provided
    if (friendlyName != null) {
      textAlign(CENTER);
      textSize(w/friendlyName.length()*1.5f);
      text(friendlyName, x, y+h, w, h);
    }
  }
}

class Input extends IODevice {
  float inputMin, inputMax;
  float lastValue = 0;
  
  public boolean isGeneratingData;

  Input (String img_src, float x, float y, float w) {
    super(img_src, x, y, w);
  }

  Input (String img_src, String img_active_src, float x, float y, float w) {
    super(img_src, img_active_src, x, y, w);
  }
  
  Input (String img_src, float x, float y, float w, String name) {
    super(img_src, x, y, w, name);
  }

  Input (String img_src, String img_active_src, float x, float y, float w, String name) {
    super(img_src, img_active_src, x, y, w, name);
  }
  
  public float generateData() {
    // Return a new value depending on the last value
    float spread = inputMax-inputMin;
    // Get a value between -10% and 10%. 
    float multiplier = random(-0.1f, 0.1f);
    // THe new value is calculated by multiplying the spread by the multiplier
    float newVal = lastValue + spread * multiplier;
    
    if (newVal >= inputMax) {
      newVal = inputMax;
    } else if (newVal <= inputMin) {
      newVal = inputMin;
    }
    
    lastValue = newVal;
    return newVal;
  }
  
  public boolean isGeneratingData() {
    return isGeneratingData;
  }
  
  public void startGeneratingData(float min, float max) {
    inputMin = min; 
    inputMax = max;
    if (lastValue == 0) lastValue = inputMin + (inputMax-inputMin)/2;
    isGeneratingData = true; 
  }
  public void stopGeneratingData() {isGeneratingData = false;}
  
  public void toggleGeneratingData() {
    if (isGeneratingData) {
      stopGeneratingData();
    } else {
      startGeneratingData(15.0f, 30.0f);
    }
  }
}



class InputButton extends Input {
  boolean mouseClicked; 
  
  InputButton (float x, float y, float w) {
    super("button.png", x, y, w);
  }
  
  InputButton (float x, float y, float w, String name) {
    super("button.png", x, y, w, name);
  }

  public boolean isPressed () {
    return (mouseX > x && mouseX < x+w && mouseY > y && mouseY < y+h); 
  }
}



class Thermometer extends Input {    
  Thermometer (float x, float y, float w) {
    super("thermometer.png", x, y, w);
  }  
}

class Output extends IODevice {  
  Output (String img_src, float x, float y, float w) {
    super(img_src, x, y, w);
  }
  
  Output (String img_src, String img_active_src, float x, float y, float w) {
    super(img_src, img_active_src, x, y, w);
  }
  
  Output (String img_src, float x, float y, float w, String name) {
    super(img_src, x, y, w, name);
  }
  
  Output (String img_src, String img_active_src, float x, float y, float w, String name) {
    super(img_src, img_active_src, x, y, w, name);
  }
}



class Lamp extends Output {
  Lamp (float x, float y, float w) {
    super("lamp.png", "lamp_on.png", x, y, w);
  }
}



class LED extends Output {
  int ledColor;

  LED (float x, float y, float w) {
    super("led-strip.png", "led-strip_on.png", x, y, w);    
  }
  
  public void changeColor() {
    println("Skifter farve");
  }
}



class Speaker extends Output { 
  float vol;

  Speaker (float x, float y, float w) {
    super("speaker_mute.png", "speaker.png", x, y, w);    
  }
  
  public float getVolume() {
    return vol;
  }
    
  public void changeVolume(float vol) {
    // Do not exceed 100% or below 0%
    if (this.vol + vol >= 0 && this.vol + vol <= 100) {
      this.vol += vol;
    }
  }
  
}
  public void settings() {  size(1800, 950); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "main" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
