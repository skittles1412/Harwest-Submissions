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

int l, r;
long memo[31][2][2][2][2];

// bitwise operators too hard
int get(int mask, int bit) {
	return (mask >> bit) & 1;
}

long dp(int ind, bool alo, bool blo, bool ahi, bool bhi) {
	if(ind == -1) {
		return 1;
	}else if(memo[ind][alo][blo][ahi][bhi] != -1) {
		return memo[ind][alo][blo][ahi][bhi];
	}
	long ans = 0;
	int cl = get(l, ind), cr = get(r, ind);
	for(int a = 0; a < 2; a++) {
		for(int b = 0; b < 2; b++) {
			if(a == 1 && b == 1) {
				continue;
			}
			if(alo && !(a >= cl)) {
				continue;
			}
			if(blo && !(b >= cl)) {
				continue;
			}
			if(ahi && !(a <= cr)) {
				continue;
			}
			if(bhi && !(b <= cr)) {
				continue;
			}
			ans += dp(ind - 1, alo && a == cl, blo && b == cl, ahi && a == cr, bhi && b == cr);
		}
	}
	return memo[ind][alo][blo][ahi][bhi] = ans;
}

void solve() {
	cin >> l >> r;
	memset(memo, -1, sizeof(memo));
	cout << dp(30, true, true, true, true) << endl;
}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(ios::failbit);
#ifdef LOCAL
	freopen("input.txt", "r", stdin);
#endif
	int t = 1;
	cin >> t;
	for(int _ = 1; _ <= t; _++) {
		dbg(_);
//		cout << "Case #" << _ << ": ";
		solve();
	}
}
