#include <iostream>
#include<stdlib.h>
#include <list> 
#include <iterator> 
#include <string>
#include <utility>
#include <exception>
using namespace std;

list<pair<string, int>> create()
{
    cout<<"Creat the list"<<endl;
    list<pair<string, int>> namelist;
    return namelist;
};

void enlist(list<pair<string, int>> &namelist, string name, int p)
{   
    pair<string,int> element (name, p);
    namelist.push_back(element);
    cout<<"The name of "<<element.first<<" has been enlisted"<<endl;
};
    
pair<string, int> delist(list<pair<string, int>> &namelist)
    {
        if (namelist.empty()== 1)
        {
            throw std::invalid_argument("The list is empty now");
        }
        pair<string, int> toRemove = namelist.front();
        int max = toRemove.second;
        list<pair<string,int>>::iterator iter = namelist.begin();
        while(iter != namelist.end()) { 
            pair<string,int>& temp = *iter;
            if (temp.second > max) {
                toRemove = temp;
                max = temp.second;
            } 
            iter++;
        }
        cout<<"Delist: "<<toRemove.first<<endl;
        namelist.remove(toRemove);
        return toRemove;
    };
    
    void empty(list<pair<string, int>> &namelist)
    {
        namelist.clear();
    };

int main() {
	list<pair<string, int>> namelist = create();
	enlist(namelist, "joe", 1);
	enlist(namelist, "Mary", 10);
	enlist(namelist, "Tom", 5);
	enlist(namelist, "gwu", 6);
	delist(namelist);
	delist(namelist);
	delist(namelist);
	empty(namelist);
	delist(namelist);
	return 0;
}
