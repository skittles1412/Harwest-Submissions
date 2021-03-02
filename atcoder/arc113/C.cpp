#include "bits/stdc++.h"

using namespace std;

//sad; long long double doesn't exist
using ld = long double;

//imagine a language where int = long
#define long long long

//typing too hard
#define endl "\n"

#define sz(x) int(x.size())

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
	string s;
	cin >> s;
	int n = sz(s);
	int arr[n];
	for(int i = 0; i < n; i ++) {
		arr[i] = s[i] - 'a';
	}
	int last = 26, lind = n - 1;
	long ans = 0;
	int cnt[27];
	memset(cnt, 0, sizeof(cnt));
	for(int i = n - 1; i >= 0; i --) {
		if(i + 1 < n && arr[i] == arr[i + 1]) {
			ans += n - i - 1 - cnt[arr[i]];
			cnt[last] -= n - lind - 1;
			for(int j = lind; j >= i; j --) {
				cnt[arr[j]] --;
			}
			last = arr[i];
			lind = i - 1;
			cnt[last] += n - lind - 1;
		}
		cnt[arr[i]] ++;
	}
	cout << ans << endl;
}