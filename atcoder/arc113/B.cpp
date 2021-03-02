#include "bits/stdc++.h"

using namespace std;

//sad; long long double doesn't exist
using ld = long double;

//imagine a language where int = long
#define long long long

//typing too hard
#define endl "\n"

#define sz(x) int(x.size())

long pow(long a, long b, long mod) {
	long res = 1, cur = a;
	while(b) {
		if(b & 1) {
			res = (res * cur) % mod;
		}
		cur = (cur * cur) % mod;
		b /= 2;
	}
	return res;
}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
//	for(int i = 1; i<=5; i++) {
//		for(int j = 1; j<=5; j++) {
//			cerr<<i<<" "<<j<<": "<<pow(i, j, 1e9)<<endl;
//		}
//	}
	long a, b, c;
	cin >> a >> b >> c;
	long cur = a % 10;
	vector<int> order;
	while(find(begin(order), end(order), cur) == end(order)) {
		order.push_back(cur);
		cur = (cur * a) % 10;
	}
	int n = sz(order);
	cout << order[(n - 1 + pow(b, c, sz(order))) % n] << endl;
}