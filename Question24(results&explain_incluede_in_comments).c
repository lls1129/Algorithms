//*******************************************************//
//*      The code of question 24                        *//
//*      Linsheng Liu                                   *//
//*******************************************************//

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

//Declare array by heap. 
char *DeclareArrayHeap(int count){
    char *arrayinheap = malloc(count);
    if(!arrayinheap)
        return NULL;
    //free space. 
    free(arrayinheap);
    return 0;

}

//Declare array by stack. No free() needed. 
char DeclareArrayStack(int count){
    char arrayinstack[count];
    return 0;
}


int main() {
    clock_t start, end;
    double cpu_time_used;

    start = clock();
    for(int i = 1; i < 100000000; i++)
        DeclareArrayHeap(10000000);
    end = clock();
    cpu_time_used = ((double)(end-start))/CLOCKS_PER_SEC;
    printf("Declare a array size of 10000000 in heap for 100000000 times took %f seconds to execute. \n", cpu_time_used);
    start = clock();
    for(int i = 1; i < 100000000; i++)
        DeclareArrayStack(10000000);
    end = clock();
    cpu_time_used = ((double)(end-start))/CLOCKS_PER_SEC;
    printf("Declare a array size of 10000000 in stack for 100000000 times took %f seconds to execute. \n", cpu_time_used);
    return 0;
}

//********************************************************//
//*Results and explainasion:                             *// 
//*The result of execute time of declaring arraies       *//
//*  by heap is  3.748 seconds, and                      *//
//*  by stack is 0.025874 seconds.                       *//
//*The conclusion is that stack is faster than heap.     *//
//*It is because that allocate and deallocate memory     *// 
//*  in a stack is just an operation of a pointer. Since *//
//*  stack uses LIFO in memory, execute the declare      *//
//*  and free needs only move pointer next one or back,  *//  
//*  in detail is increment and decrement of an integer. *// 
//*  That would take very short time.                    *//
//*  However, heap is relatively complex when allocating *//
//*  and deallocating memory. Heap has dynamic memory    *// 
//*  allocation and each time allocating memory takes    *//  
//*  run time as the size of array, as well as free it.  *//
//*  Comparing to the LIFO of stack, elements of heap    *//  
//*  has no dependencies with each other. That makes it  *//
//*  more complex to keep track which parts of heap are  *//  
//*  allocated or free at any given time.                *// 
//********************************************************//
