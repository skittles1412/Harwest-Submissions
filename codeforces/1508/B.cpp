#include "bits/extc++.h"

using namespace std;

//imagine a language where int = long
#define long int64_t

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

int csz;
vector<int> ans;
set<int> lvs;

void solve(long k) {
	if(csz == 0) {
		return;
	}
	int p = ans.empty() ? 0 : ans.back();
	if(k == 0 || csz > 61) {
		ans.push_back(*lvs.lower_bound(p - 1));
		lvs.erase(ans.back());
		csz--;
		solve(k);
		return;
	}
	long prev = 0;
	long cur = 0;
	vector<int> vals;
	for(auto &a: lvs) {
		if(a >= p - 1) {
			vals.push_back(a);
		}
	}
	long c = csz - 2;
	for(int i = 0; i < csz; i++) {
		prev = cur;
		cur += 1LL << c;
		if(cur > k) {
			ans.push_back(vals[i]);
			lvs.erase(find(begin(lvs), end(lvs), ans.back()));
			csz--;
			solve(k - prev);
			return;
		}
		c = max(long(0), c - 1);
	}
	assert(false);
}

void solve() {
	int n;
	long k;
	cin >> n >> k;
	if(n < 62 && (1LL << (n - 1)) < k) {
		cout << -1 << endl;
		return;
	}
	for(int i = 0; i < n; i++) {
		lvs.insert(i + 1);
	}
	csz = n;
	ans.clear();
	solve(k - 1);
	for(auto &a: ans) {
		cout << a << " ";
	}
	cout << endl;
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
