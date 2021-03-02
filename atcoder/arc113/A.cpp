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
	int k;
	cin >> k;
	int cnt[k + 1];
	memset(cnt, 0, sizeof(cnt));
	for(int i = 0; i <= k; i ++) {
		for(int j = 1; j * j <= i; j ++) {
			if(i % j == 0) {
				cnt[i] ++;
				if(j * j != i) {
					cnt[i] ++;
				}
			}
		}
	}
	int psum[k + 1];
	psum[0] = cnt[0];
	for(int i = 1; i <= k; i ++) {
		psum[i] = psum[i - 1] + cnt[i];
	}
	long ans = 0;
	for(int i = 1; i <= k; i ++) {
		ans += psum[k / i];
	}
	cout << ans << endl;
}