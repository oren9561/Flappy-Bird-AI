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

public class flappy_bird_AI extends PApplet {

//Global variables
PImage midFlap, downFlap, upFlap, background, start, lose, pipe, pipeDown, ground;
PFont f;
float[] floor;
int score, gap, closestPipe, gen = -1, highScore = 0;
Pipe[] p = new Pipe[5];
float startTime;
Population pop; 
Brain[] b;

public void setup(){
  
  
  //loading images, variables and text
  
  gap = 426;                                      //control the difficulty by increasing/decreasing the gap between the pipes
  midFlap = loadImage("midFlap.png");
  upFlap = loadImage("upFlap.png");
  downFlap = loadImage("downFlap.png");
  background = loadImage("background1.png");
  lose = loadImage("lose.png");
  start = loadImage("start.png");
  pipe = loadImage("pipe.png");
  pipeDown = loadImage("pipeDown.png");
  ground = loadImage("ground.png");
  f = createFont("font.TTF", 70, true);
  textFont(f, 70);
  score = 0;
  closestPipe = 0;
  startTime = millis();
  gen++;
  
  //making the floor
  
  floor = new float[6];
  floor[0] = -336;
  for(int i=1; i<6; i++)
     floor[i] = floor[i-1] + 336;
     
  //making the pipes
  
  for(int i=0; i<5; i++){
     int rand = (int)random(-260, -32);
     p[i] = new Pipe(1200 + 250*i, rand, rand+gap, pipeDown, pipe);
  }
  
  //updating the brains to the current population's brains
  ////making the population of birds (size of population can be changed in the brackets of the function)
  
  if(gen==0 || highScore == 0) pop = new Population(100);
  b = pop.getBrains();

}

public void draw(){
  loadBG();
  for(int i=0; i<pop.getSize(); i++){
     Bird bird = b[i].getBird();
     if(!bird.isDead()){
       bird.setDead(bird.checkDead(p));
       if(b[i].activation() && (millis()/100 - bird.getLastTimeJumped()/100) > 3) bird.jump();
       bird.update();
     }
  }
  updateClosestPipe();
  if(pop.allDead()){
   pop.evolve();
   if(score > highScore) highScore = score;
   setup();
  }
}

public void loadBG(){
 background(background); 
 for(int i=0; i<5; i++){
   p[i].update();
 }
 for(int i=0;i<6;i++){
    if(i==0){
      image(ground, floor[i], 467); 
      floor[i] -= 2.5f;
      if(floor[i]<-350) floor[i] = (int)floor[5] + 330;
    }
    else{
      image(ground, floor[i], 467); 
      floor[i] -= 2.5f;
      if(floor[i]<-350) floor[i] = (int)floor[i-1] + 330;
    }
 }
 text(score, 484, 100);                     //current score
 textSize(30);
 text(highScore + " - HighScore", 8, 62);   //highest score
 text(gen + " - gen", 8, 30);               //current gen
 textSize(70);
}

public void updateClosestPipe(){ //updates the index of the pipe which is currently the closest to the birds
  if(!p[closestPipe].isFresh()){
     if(closestPipe == 4) closestPipe = 0;
     else closestPipe++;
  }
}
class Bird{
 
  float flap;
  int i;
  float angle;
  PVector v;
  PVector p;
  PImage img;
  PImage[] images;
  boolean dead;
  boolean a;
  int score1;
  float lastTimeJumped;
  
  
  public Bird(PImage midFlap, PImage upFlap, PImage downFlap){
    this.lastTimeJumped = 0;
    this.a = true;
    this.dead = false;
    this.angle = 0;
    this.flap = 0;
    this.i = 3;
    this.v = new PVector(-3, 0);
    this.p = new PVector(200, 230);
    this.images = new PImage[4]; 
    images[0] = upFlap;
    images[1] = midFlap;
    images[2] = downFlap;
    images[3] = midFlap;
    this.img = images[i];
    this.score1 = 0;
  }
  
  public float getY(){
   return this.p.y; 
  }
  
  public float getX(){
   return this.p.x; 
  }
  
  public int getScore(){
   return this.score1; 
  }
  
  public void show(){
    pushMatrix();
    this.move();
    image(this.img, 0, 0);
    popMatrix();  
    this.flap();
    this.score1 = score;
  }
  
  public void flap(){
    float mil = millis()/100;
    if(mil - this.flap > 0.5f){
      this.i++;
      if(this.i==4) this.i = 0;
      this.img = this.images[this.i];
      this.flap = mil;
    }
  }
  
  public boolean checkDead(Pipe[] p){
   float x = this.p.x + this.img.width/2;
   float y = this.p.y;
   if(y+this.img.height/3 - 3>=456 || y+this.img.height/3 - 3<=-100) return true;
   for(int i=0;i<5;i++){
     if((x>=p[i].getX() && x<=(p[i].getX()+pipe.width)) && !(y>(p[i].getYUp()+pipe.height) && y<p[i].getYDown()))
       return true;
     y += this.img.height/3 - 3;
     if((x>=p[i].getX() && x<=(p[i].getX()+pipe.width)) && !(y>(p[i].getYUp()+pipe.height) && y<p[i].getYDown()))
       return true;
   }
   return false;
  }
  
  public void setDead(boolean a){
   this.dead = a; 
  }
  
  public void update(){
   if(!dead) this.show();
  }
  
  public boolean isDead(){
   return this.dead; 
  }
  
  public float getLastTimeJumped(){
    return this.lastTimeJumped;
  }
  
  public void jump(){ // sets the velocity of the bird to be positive (as if it jumps forcefully all of a sudden)
   this.v.x = 13;
   this.lastTimeJumped = millis();
  }
  
  public void move(){ // moves up and down accordingly to the acceleration and speed of the bird (v.y = acceleration, v.x = velocity)
      if(this.v.x>0){ // happens only when the bird jumps
          this.p.y -= this.v.x;
          this.v.x = this.v.x/1.2f;
          this.rotateUp();
          if(this.v.x <=0.5f)
            this.v.x = -3;
      }
      else{ // happens only when the bird falls (doesn't jump) 
        this.p.y -= this.v.x;
        this.rotateDown();
        if(this.v.x > -7)
          this.v.x -= 0.4f;
      }
  }
  
  public void rotateUp(){
    if(!this.a){
    if(this.angle<=0){
      this.angle = 0;
      translate(this.p.x, this.p.y);
    }
    else{
      if(this.angle<=15){
        this.angle = 15;
        translate(this.p.x-2, this.p.y+3);
      } 
      else{
          if(this.angle<=30){
            this.angle = 30;
            translate(this.p.x-3, this.p.y+8); 
          }
          else {
            this.angle = 45; 
            translate(this.p.x-6, this.p.y+13);
          }
      }
    }
    this.a = true;
    }
    else{
    switch((int)this.angle){
     case 0: this.angle = 15;  translate(this.p.x-2, this.p.y+3);   break;
     case 15: this.angle = 30; translate(this.p.x-3, this.p.y+8);   break;
     case 30: this.angle = 45; translate(this.p.x-6, this.p.y+13);   break;
     case 45: translate(this.p.x-6, this.p.y+13); break;
    }
    }
    rotate(radians(-this.angle));
  }
  
  public void rotateDown(){
    this.a = false;
    switch((int)this.angle/10){
     case 0: translate(this.p.x, this.p.y);          break;
     case 1: translate(this.p.x-1, this.p.y+1);      break;
     case 2: translate(this.p.x-2, this.p.y+4);      break;
     case 3: translate(this.p.x-3, this.p.y+8);      break;
     case 4: translate(this.p.x-4, this.p.y+12);     break;
     case -1: translate(this.p.x, this.p.y-2);       break;
     case -2: translate(this.p.x+3, this.p.y-4);     break;
     case -3: translate(this.p.x+6, this.p.y-6);     break;
     case -4: translate(this.p.x+9, this.p.y-8);     break;
     case -5: translate(this.p.x+13, this.p.y-10);   break;
     case -6: translate(this.p.x+18, this.p.y-10);   break;
     case -7: translate(this.p.x+21, this.p.y-10);   break;
     case -8: translate(this.p.x+24, this.p.y-8);    break;
     case -9: translate(this.p.x+28, this.p.y-7);    break;
    }
    if(this.angle>=0) this.angle -= 2.2f;
    else
      if(this.angle>-90) this.angle -= 10;
    rotate(radians(-this.angle));
  }
  
  
  
}
class Brain{
 
  Bird b;
  float[] w;  //weights
  float bias; //bias
  
  public Brain(Bird b){ //builds a brand new brain - used only for gen 0
    this.b = b;
    this.w = new float[3];
    for(int i=0;i<3;i++){
      this.w[i] = random(-3, 1.5f);
      while(this.w[i] == 0) this.w[i] = random(-3, 1.5f);
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
      this.w[i] += random(-1.5f, 0.5f);
      while(this.w[i] == 0) this.w[i] += random(-1.5f, 0.5f);
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
   double tanh = (2/(1 + Math.pow(2.718f, wSum*(-2)))) -1;
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
class Pipe{
 
  boolean fresh;
  float x;
  int yup;
  int ydown;
  float xspeed;
  PImage pipeDown;
  PImage pipe;
  
  public Pipe(float x, int yup, int ydown, PImage pipeDown, PImage pipe){
    this.x = x;
    this.yup = yup;
    this.ydown = ydown;
    this.pipeDown = pipeDown;
    this.pipe = pipe;
    this.xspeed = -2.5f;
    this.fresh = true;
  }
  
  public void show(){
   image(this.pipeDown, this.x, this.yup); 
   image(this.pipe, this.x, this.ydown); 
  }
  
  public void update(){
    this.show();
    this.x += this.xspeed;
    
    //score system
    
    if(this.x<160 && this.fresh){
      score++;
      this.fresh = false;
    }
    
    //renewing the pipes
    
    if(this.x<-55.0f){
      this.x = 1200.0f;
      this.yup = (int)random(-260, -32);
      this.ydown = this.yup + gap;
      this.fresh = true;
    }
  }
  
  public int getYUp(){
   return this.yup; 
  }
  
  public int getYDown(){
   return this.ydown; 
  }
  
  public float getX(){
   return this.x; 
  }
  
  public boolean isFresh(){
   return this.fresh; 
  }
  
}
class Population{
 
  Brain[] b;
  int size;
  
  public Population(int size){ //makes a whole new population of birds with default - random brains
   this.size = size;
   this.b = new Brain[size];
   for(int i=0;i<size;i++){
    Bird b = new Bird(midFlap, upFlap, downFlap);
    this.b[i] = new Brain(b);
   }
  }
  
  public boolean allDead(){ //checks if all the birds are dead
   for(int i=0;i<this.size;i++)
     if(!this.b[i].getBird().isDead())
       return false;
   return true;
  }
  
  public void evolve(){ //when all the birds are dead, this function makes a new population (next gen) with the information from the last one
    Brain b = this.naturalSelection();
    Bird bird = new Bird(midFlap, upFlap, downFlap);
    if(b.fitness()<this.b[0].fitness()) this.b[0] = new Brain(this.b[0], bird);
    else this.b[0] = new Brain(b, bird);
    for(int i=1;i<this.size;i++){
      bird = new Bird(midFlap, upFlap, downFlap);
      this.b[i] = new Brain(b, bird);
      this.b[i].mutate();
    }
  }
  
  public Brain naturalSelection(){ //checks and returns the bird that lasted longest - with the best fitness
   float best = this.b[0].fitness();
   int bestI = 0;
   for(int i=1;i<this.size;i++){
    if(this.b[i].fitness() > best){
     best = this.b[i].fitness();
     bestI = i;
    }
   }
   return this.b[bestI];
  }
  
  public Brain[] getBrains(){
   return this.b; 
  }
  
  public int getSize(){
    return this.size;
  }
  
}
  public void settings() {  size(1024, 576); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "flappy_bird_AI" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
