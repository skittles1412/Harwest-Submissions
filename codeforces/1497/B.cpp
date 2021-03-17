#include "bits/stdc++.h"

using namespace std;

//sad; long long double doesn't exist
using ld = long double;

//imagine a language where int = long
#define long long long

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

void solve() {
	int n, m;
	cin >> n >> m;
	int arr[n], cnt[m] {};
	for(int i = 0; i < n; i++) {
		cin >> arr[i];
		cnt[arr[i] % m]++;
	}
	int ans = 0;
	for(int i = 0; i <= m / 2; i++) {
		int j = (m - i) % m;
		if(i == j) {
			ans += bool(cnt[i]);
		}else {
			if(max(cnt[i], cnt[j]) > 0) {
				ans += max(1, abs(cnt[i] - cnt[j]));
			}
		}
	}
	cout << ans << endl;
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
