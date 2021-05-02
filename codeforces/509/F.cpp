#include "bits/extc++.h"

using namespace std;

template<class T, class U = less<T>>
using rt = __gnu_pbds::tree<T, __gnu_pbds::null_type, U, __gnu_pbds::rb_tree_tag, __gnu_pbds::tree_order_statistics_node_update>;

template<class T, class U>
void sep(T &out, const string &s, const U &u) {
	out << u;
}

template<class T, class Head, class ...Tail>
void sep(T &out, const string &s, const Head &h, const Tail &...t) {
	out << h << s;
	sep(out, s, t...);
}

#ifdef DEBUG
#define dbg(...)                                                      \
cerr << "L" << __LINE__ << " [" << #__VA_ARGS__ << "]" << ": ";       \
sep(cerr, " | ", __VA_ARGS__);                                        \
cerr << endl
#else
#define cerr if(false) cerr
#define dbg(...) cerr
#endif

//imagine a language where int = long
#define long int64_t

//typing too hard
#define endl "\n"

#define sz(x) int((x).size())

const long mod = 1e9+7;

void solve() {
	int n;
	cin >> n;
	if(n == 1) {
		cout << 1 << endl;
		return;
	}
	int arr[n];
	for(int i = 0; i < n; i++) {
		cin >> arr[i];
	}
	long dp[n + 2][n] {};
	for(int i = 1; i <= n; i++) {
		dp[i][i - 1] = 1;
	}
	for(int i = n - 1; i >= 0; i--) {
		for(int j = i; j < n; j++) {
			dp[i][j] = dp[i + 1][j];
			for(int k = i + 1; k <= j; k++) {
				if(arr[k] > arr[i]) {
					dp[i][j] = (dp[i][j] + dp[i + 1][k - 1] * dp[k][j]) % mod;
				}
			}
		}
	}
	cout << dp[1][n - 1] << endl;
}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
	int t = 1;
//	cin >> t;
	for(int _ = 1; _ <= t; _++) {
		dbg(_);
//		cout << "Case #" << _ << ": ";
		solve();
	}
}
