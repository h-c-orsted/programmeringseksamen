
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
  
  
  void update() {
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
      textSize(w/friendlyName.length()*1.5);
      text(friendlyName, x, y+h, w, h);
    }
  }
}
