Controller ctrl;


Graph tempGraph = new Graph(1150, 20, 600, 400, "Temperatur", 50);
Graph lampGraph = new Graph(50, 200, 400, 200, "Lys tændt/slukket", 30);

Automation automation = new Automation();
Automation tempAutomation = new Automation();
Automation speakerAutomation = new Automation();

float iconSize = 100;


void setup() {
  size(1800, 950);

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



void draw() {
  clear();
  background(200);
  
  textAlign(LEFT);
  textSize(50);
  text("Stue", 50, 70);
  
  // Display controller and update everything in automations and graphs
  ctrl.update();
}
