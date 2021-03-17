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
	int n;
	cin >> n;
	int arr[n];
	for(int i = 0; i < n; i++) {
		cin >> arr[i];
	}
	sort(arr, arr + n);
	vector<int> ans, l;
	ans.reserve(n);
	l.reserve(n);
	int prev = -1;
	for(int i = 0; i < n; i++) {
		if(prev != arr[i]) {
			ans.push_back(arr[i]);
		}else {
			l.push_back(arr[i]);
		}
		prev = arr[i];
	}
	ans.insert(ans.end(), begin(l), end(l));
	for(int i = 0; i < n; i++) {
		cout << ans[i] << " ";
	}
	cout << endl;
//	int ans = 0;
//	set<int> cur;
//	for(int i = 0; i <= 200; i++) {
//		cur.insert(i);
//	}
//	for(int i = 0; i < n; i++) {
//		cur.erase(arr[i]);
//		ans += *cur.begin();
//	}
//	cout << ans << endl;
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
