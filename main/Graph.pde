
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
  
  
  void addData(float val) {
    // Move every value one step forward
    for (int i=0; i<data.length-1; i++) {
      data[i] = data[i+1];
    }
    // Add the value to the end of the array
    data[data.length-1] = val;
  }
  
  
  void show() {
    // Setup lines and stuff
    graphLayout();
    displayData();
  }
  
  
  void graphLayout () {
    textAlign(LEFT);
    stroke(0);
    fill(0);
    rect(x, y, 2, h);
    rect(x, y+h, w, 2);
    textSize(25);
    text(title, x+10, y+20);
  }
  
  
  void displayData() {
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
      float pointY = y + h - (h/(maxVal*1.2)) * data[i];
      pointY = pointY > 0 ? pointY : y+h;
      
      // Next value (for drawing line)
      float point1X = x + dataWidth*(i+1) + 1;
      float point1Y = y + h - (h/(maxVal*1.2)) * data[i+1];
      point1Y = point1Y > 0 ? point1Y : y+h;
      
      line(pointX, pointY, point1X, point1Y);
    }
  }
  
  
}
