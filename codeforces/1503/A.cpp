#include "bits/stdc++.h"

using namespace std;

//imagine a language where int = long
#define long int64_t

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

bool check(const vector<int> &arr) {
	int psum = 0;
	for(auto &a: arr) {
		psum += a;
		if(psum < 0) {
			return false;
		}
	}
	return psum == 0;
}

void solve() {
	int n;
	cin >> n;
	string s;
	cin >> s;
	vector<int> aind, bind;
	for(int i = 0; i < n; i++) {
		if(s[i] == '1') {
			aind.push_back(i);
		}else {
			bind.push_back(i);
		}
	}
	if(sz(aind) % 2 != 0) {
		cout << "NO" << endl;
		return;
	}
	vector<int> a(n), b(n);
	for(int i = 0; i < sz(aind) / 2; i++) {
		a[aind[i]] = 1;
		b[aind[i]] = 1;
	}
	for(int i = sz(aind) / 2; i < sz(aind); i++) {
		a[aind[i]] = -1;
		b[aind[i]] = -1;
	}
	for(int i = 0; i < sz(bind); i++) {
		if(i % 2) {
			a[bind[i]] = 1;
			b[bind[i]] = -1;
		}else {
			a[bind[i]] = -1;
			b[bind[i]] = 1;
		}
	}
	if(check(a) && check(b)) {
		cout << "YES" << endl;
		for(auto &c: a) {
			if(c == 1) {
				cout << "(";
			}else {
				cout << ")";
			}
		}
		cout << endl;
		for(auto &c: b) {
			if(c == 1) {
				cout << "(";
			}else {
				cout << ")";
			}
		}
		cout << endl;
	}else {
		cout << "NO" << endl;
	}
}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
	int t;
	cin >> t;
	while(t--) {
		solve();
	}
}
