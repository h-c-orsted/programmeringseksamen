
class Controller {
  float aspectRatio = 0.70859375;
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
  
  void display() {image(img, x, y, w, h);}
  
  void addAutomation(Automation a) {
    automationsList.add(a);
  }
  
  void addGraph(Graph g) {
    graphsList.add(g);
  }
  
  void update() {
    display();
    for (Automation a : automationsList) {a.update();}
    for (Graph g : graphsList) {g.show();}
  }
}
