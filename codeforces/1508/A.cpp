#include "bits/stdc++.h"

using namespace std;

//imagine a language where int = long
#define long int64_t

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

string solve(string a, string b, char c) {
	int n = sz(a);
	string ans;
	int aind = 0, bind = 0;
	while(min(aind, bind) < n) {
		if(aind == n) {
			ans += b[bind++];
		}else if(bind == n) {
			ans += a[aind++];
		}else {
			char ac = a[aind], bc = b[bind];
			if(ac != bc) {
				if(ac == c) {
					ans += ac;
					aind++;
				}else {
					ans += bc;
					bind++;
				}
			}else {
				ans += ac;
				aind++;
				bind++;
			}
		}
	}
	return ans;
}

void solve() {
	int n;
	cin >> n;
	string arr[3];
	for(int i = 0; i < 3; i++) {
		cin >> arr[i];
	}
	vector<string> ans;
	for(int i = 0; i < 3; i++) {
		for(int j = i + 1; j < 3; j++) {
			ans.push_back(solve(arr[i], arr[j], '0'));
			ans.push_back(solve(arr[i], arr[j], '1'));
		}
	}
	sort(begin(ans), end(ans), [] (const string &a, const string &b) {
		return sz(a) < sz(b);
	});
	assert(sz(ans[0]) <= 3 * n);
	cout << ans[0] << endl;
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
