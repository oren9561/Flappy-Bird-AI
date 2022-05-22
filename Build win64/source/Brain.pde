class Brain{
 
  Bird b;
  float[] w;  //weights
  float bias; //bias
  
  public Brain(Bird b){ //builds a brand new brain - used only for gen 0
    this.b = b;
    this.w = new float[3];
    for(int i=0;i<3;i++){
      this.w[i] = random(-3, 1.5);
      while(this.w[i] == 0) this.w[i] = random(-3, 1.5);
    }
    this.bias = random(-500, 500);
    while(this.bias == 0) this.bias = random(-500, 500);
  }
  
  public Brain(Brain brain, Bird b){ //clones a brain from another brain-+
    this.b = b;
    this.w = new float[3];
    for(int i=0;i<3;i++)
      this.w[i] = brain.getW()[i];
    this.bias = brain.getBias();
  }
  
  public void mutate(){ //"mutates" a given brain by randomly tweaking it's weights and bias a little
    for(int i=0;i<3;i++){
      this.w[i] += random(-1.5, 0.5);
      while(this.w[i] == 0) this.w[i] += random(-1.5, 0.5);
    }
    this.bias += random(-20, 20);
    while(this.bias == 0) this.bias += random(-20, 20);
  }
  
  public float fitness(){ //if the bird is dead this fitness function is called which determines the "fitness" of the bird by how long it lived during the gen run
    return this.b.getScore();
  }
  
  public boolean activation(){ //activation function - if returned true then the bird jumps
   float by = this.b.getY();                                 //bird Y
   float bx = this.b.getX();                                 //bird X
   float pyUp = p[closestPipe].getYUp() + pipe.height;       //pipe yUp
   float pyDown = p[closestPipe].getYDown();                 //pipe yDown
   float px = p[closestPipe].getX();                         //pipe X
   float dUp = dist(bx, by, px, pyUp);
   float dDown = dist(bx, by, px, pyDown);
   float wSum = (by*this.w[0]) + (dUp*this.w[1]) + (dDown*this.w[2]) + this.bias;
   double tanh = (2/(1 + Math.pow(2.718, wSum*(-2)))) -1;
   line(bx, by, px, pyUp);
   line(bx, by, px, pyDown);
   if(tanh>0) return true;
   return false;
  }
  
  public float getBias(){
   return this.bias;
  }
  
  public float[] getW(){
   return this.w; 
  }
  
  public Bird getBird(){
   return this.b; 
  }
  
}
