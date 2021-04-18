
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
    float multiplier = random(-0.1, 0.1);
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
      startGeneratingData(15.0, 30.0);
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
