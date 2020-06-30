#include <bits/stdc++.h>
#define REDIRECT
#undef REDIRECT
using namespace std;

int main(){
	#ifdef REDIRECT
		freopen("Input.txt","r",stdin);
		freopen("Output.txt","w",stdout);
		freopen("Error.txt","w",stderr);
	#endif
	int w;
	cin>>w;
	if(w<4||w&1){
		cout<<"NO";
	}else{
		cout<<"YES";
	}
	return 0;
}