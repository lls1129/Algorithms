//The Question 25
#include <iostream>
#include <stdlib.h>
#include <ctype.h>
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
    cout<<"Create The List\n";
};

void enlist(string n, int p)
{
    node* current = head;
    while (current->next != NULL)
    {
        current = current->next;
    }
    node* NewNode = new node();
    NewNode->name = n;
    NewNode->priority = p;
    current->next = NewNode;
    NewNode->next = NULL;
    cout<<"The element named "<<NewNode->name<<" with the priority of "<<NewNode->priority<<" has been enlisted\n";
};

node* delist()
{
    try {
        if (head->next == NULL) {
            throw std::invalid_argument("The list is empty now");
        }
        int globalmax = 0;
        node *current = head->next;
        node *max = head->next;
        node *previous = head;
        while (current->next != NULL) {
            current = current->next;
            if (current->priority > globalmax) {
                globalmax = current->priority;
                max = current;
                previous = previous->next;
            }
        }
        string res = max->name;

        previous->next = max->next;
        max->next = NULL;
        cout << "The element named " << res << " is removed.\n";
    }
    catch(exception &e){
        cout<<"(Exception)The list is empty now\n";
    }
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
    enlist("mary", 10);
    enlist("tom", 5);
    enlist("gwu", 6);
    delist();
    delist();
    delist();
    empty();
    delist();
    return 0;
}
