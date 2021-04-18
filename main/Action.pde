
class Action {
  void execute() {
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
