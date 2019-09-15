
#include <iostream>
#include<stdlib.h>
#include<ctype.h>
#include <exception>
using namespace std;
struct node
{
    string name;
    int priority;
    node* next;
};

void create();
void enlist(string name, int p);
node* delist();
void empty();

node* head = new node;
void create()
{
    cout<<"Activate the list"<<endl;
};

void enlist(string n, int p)
{   
    node* cur = head;
    while (cur->next != NULL) 
    {
          cur = cur->next;
    }
    node* newNode = new node();
    newNode->name = n;
    newNode->priority = p;
    cur->next = newNode;
    newNode->next = NULL;
    cout<<"The name of "<<newNode->name<<" has been enlisted"<<endl;
};
    
    node* delist()
    {
        if (head->next == NULL)
        {
            throw std::invalid_argument("The list is empty now");
        }
        int global_Max = 0;
        node* cur = head->next;
        node* max = head->next;
        node* prev = head;
        while (cur->next != NULL)
        {
            cur = cur->next;
            if (cur->priority > global_Max)
            {
                global_Max = cur->priority;
                max = cur;
                prev = prev->next;
            }
        }
        string res = max->name;
        
        prev->next = max->next;
        max->next = NULL;
        cout<<"Delist: "<<res<<endl;
        return head->next;
    };
    
    void empty()
    {
        head->next = NULL;
    };

int main() {
	// your code goes here
	create();
	enlist("joe", 1);
	enlist("Mary", 10);
	enlist("Tom", 5);
	enlist("gwu", 6);
	delist();
	delist();
	delist();
	empty();
	delist();
	return 0;
}
