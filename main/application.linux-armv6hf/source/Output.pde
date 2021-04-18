
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
  color ledColor;

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
