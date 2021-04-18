
class Automation {
  public ArrayList<Input> listI = new ArrayList<Input>();
  public ArrayList<Output> listO = new ArrayList<Output>();
  public ArrayList<Action> listA = new ArrayList<Action>();
  
  void addInput(Input i) {
    listI.add(i);
  }
  
  void addOutput(Output o) {
    listO.add(o);
  }
  
  void addAction(Action a) {
    listA.add(a);
  }

   
  public Input getInput(int i) {
     return listI.get(i);
  }
  
  public Output getOutput(int i) {
    return listO.get(i);
  }
  
  
  void update() {
    for (Input i : listI) {i.update();}
    for (Output o : listO) {o.update();}
    for (Action a : listA) {a.execute();}
  }
}
