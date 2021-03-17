#include "bits/stdc++.h"

using namespace std;

//sad; long long double doesn't exist
using ld = long double;

//imagine a language where int = long
#define long long long

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

const int m = 3200;
vector<int> primes;

void init() {
	bool p[m + 1];
	memset(p, 1, sizeof(p));
	for(int i = 2; i <= m; i++) {
		if(p[i]) {
			primes.push_back(i);
			for(int j = i * i; j <= m; j += i) {
				p[j] = false;
			}
		}
	}
}

void solve() {
	int n, k;
	cin >> n >> k;
	int ans = 0;
	set<pair<int, vector<int>>> cnt;
	for(int i = 0; i < n; i++) {
		int x;
		cin >> x;
		vector<int> cur;
		for(auto &a: primes) {
			int cnt = 0;
			while(x % a == 0) {
				x /= a;
				cnt++;
			}
			if(cnt & 1) {
				cur.push_back(a);
			}
		}
		pair<int, vector<int>> a = {x, cur};
		if(cnt.count(a)) {
			ans++;
			cnt.clear();
		}
		cnt.insert(a);
	}
	if(sz(cnt)) {
		ans++;
	}
	cout << ans << endl;
}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
	init();
	int t;
	cin >> t;
	while(t--) {
		solve();
	}
}
