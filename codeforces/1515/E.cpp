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

const int maxn = 400;

int n;
long mod, choose[maxn + 1][maxn + 1], memo[maxn + 1][maxn + 1], xpow[maxn + 1];

long dp(int len, int cnt) {
	if(len == 0) {
		return 0;
	}else if(min(len, cnt) < 0) {
		return len == -1 && cnt == 0;
	}else if(memo[len][cnt] != -1) {
		return memo[len][cnt];
	}
	long ans = 0;
	for(int i = 1; i <= len; i++) {
		ans = (ans + choose[cnt][i] * xpow[i - 1] % mod * dp(len - i - 1, cnt - i)) % mod;
	}
	return memo[len][cnt] = ans;
}

void pcomp() {
	for(int i = 0; i <= maxn; i++) {
		choose[i][0] = choose[i][i] = 1;
		for(int j = 1; j < i; j++) {
			choose[i][j] = (choose[i - 1][j - 1] + choose[i - 1][j]) % mod;
		}
	}
}

void solve() {
	cin >> n >> mod;
	pcomp();
	xpow[0] = 1;
	for(int i = 0; i < maxn; i++) {
		xpow[i + 1] = (xpow[i] * 2) % mod;
	}
	long ans = 0;
	memset(memo, -1, sizeof(memo));
	for(int i = 0; i <= n; i++) {
		ans += dp(n, i);
		dbg(i, dp(n, i));
	}
	cout << ans % mod << endl;
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
