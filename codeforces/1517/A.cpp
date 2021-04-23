#include "bits/stdc++.h"

using namespace std;

//imagine a language where int = long
#define long int64_t

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

void solve() {
	long n;
	cin >> n;
	if(n % 2050 != 0) {
		cout << -1 << endl;
		return;
	}
	n /= 2050;
	string s = to_string(n);
	int ans = 0;
	for(auto &a: s) {
		ans += a - '0';
	}
	cout << ans << endl;
}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
	int t = 1;
	cin >> t;
	for(int _ = 1; _ <= t; _++) {
		solve();
	}
}
