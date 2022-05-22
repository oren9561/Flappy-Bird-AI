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
