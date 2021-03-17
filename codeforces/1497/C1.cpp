#include "bits/stdc++.h"

using namespace std;

//sad; long long double doesn't exist
using ld = long double;

//imagine a language where int = long
#define long long long

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

vector<int> solve(int x) {
	assert(x >= 3);
	if(x & 1) {
		return {x / 2, x / 2, 1};
	}else if(x == 4) {
		return {1, 1, 2};
	}else if(x == 6) {
		return {2, 2, 2};
	}else {
		auto ans = solve(x / 2);
		for(auto &a: ans) {
			a *= 2;
		}
		return ans;
	}
}

void solve() {
	int n, k;
	cin >> n >> k;
	auto ans = solve(n);
	cout << ans[0] << " " << ans[1] << " " << ans[2] << endl;
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
