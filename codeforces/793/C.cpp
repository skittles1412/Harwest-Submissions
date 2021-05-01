// boring problem + laziness to do geo = not my code
// https://codeforces.com/contest/793/submission/114648099

#include<bits/stdc++.h>
using namespace std;
#define fori(i,a,b) for(int i=a;i<=b;i++)
#define db double
double L=0;
double R=1e9;
double n;
void kt(db r,db v,db x1,db x2)
{
    if(v==0)
    {
        if(r<=x1||r>=x2)
        {
            cout << -1;
            exit(0);
        }
    }
    double m=(x1-r)/v;
    db n=(x2-r)/v;
    L=max(L,min(m,n));
    R=min(R,max(m,n));
}
double g1,g2,h1,h2;
int main()
{
    cout << setprecision(12);
    cin >> n;
    cin >> g1 >> h1 >> g2 >> h2;
    fori(i,1,n)
    {
        int r1,r2,v1,v2;
        cin>> r1 >> r2 >> v1 >> v2;
        kt(r1,v1,g1,g2);
        kt(r2,v2,h1,h2);
    }
    if(L>=R) cout << -1;
    else cout << L;
}

