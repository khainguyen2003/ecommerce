# **Kafka**
## Các khái niệm

- **Message queue:** Là cơ chế giao tiếp bất đồng bộ giữa các thành phần và dịch vụ khác nhau. Nó hoạt động như một nơi
trung gian lưu trữ thông điệp đến khi được xử lý bởi dịch vụ nhận
  - Tin nhắn sau khi được xử lý sẽ được xóa khỏi queue vì vậy thường được dùng để xử lý các tác vụ điều tiết với tần xuất
  cao và mỗi nhiệm vụ được thực thi 1 lần
  - Khi Queue chết và được phục hồi thì tin nhẵn lỗi có thể được khôi phục nhưng bị mất đi thứ tự.
    - MQ truyền thống lưu message trong file + bộ nhớ tạm. Quá trình đọc là đồng thời và bất đồng bộ, ban đầu trong queue
    là M1 -> M2 -> M3 -> M4, khi queue chết, M3 và M4 vẫn nằm trong buffer của producer hoặc consumer chưa ACK. Sau khi
    phục hồi, M3, M4 được gửi lại có thể thành M1 -> M2 -> M4 -> M3
    - Kafka đảm bảo đúng thứ tự trong một partition, nhưng có thể mất thứ tự nếu replication chưa đồng bộ hết khi broker
    chết hoặc producer gửi **không gắn key**

- **Streaming:** Là môi trường truyền tin nhắn cho consumer và nó có thể gửi cho tất cả các consumer đang đăng ký nhận tin nhắn. Streaming sử dụng cơ
  chế lưu trữ phân tán với log file. Vì thể consumer có thể đọc lại và xử lý lại các tin nhắn đã nhận.
  - Mỗi partition là một file log bất biến (append-only log file) trên disk<br/>
  Offset: 0   1   2   3   4<br/>
  Message: M1  M2  M3  M4  M5
  - Mỗi consumer group lưu offset hiện tại (ví dụ offset = 2 nghĩa là đã đọc M1, M2).
  - Kafka luôn gửi message theo thứ tự offset tăng dần.

=> Kafka là một môi trường truyền tin, hệ thống pub sub  bất đồng bộ hỗ trợ cả Message Queue và Streaming.

- Kraf (bản mới) / zookeeper (bản cũ): 
  - Trước đây, zookeeper quản lý metadata (topic, partition, broker list, leader election).
  - Bây giờ: Kafka tự quản lý metadata
- **Cluster:** là một cụm các broker hoạt động cùng nhau để xử lý yêu cầu của producer và consumer. Trong cluster sẽ có một
consumer làm leader. Leader sẽ theo dõi trạng thái của các broker khác và bầu leader mới cho partition khi leader chết
  - Trong cluster dữ liệu của các broker sẽ được chia sẻ với nhau để khi một trong các broker chết thì sẽ luôn có một broker khác lên thay thế và đảm nhận lại partition của broker bị chết.
    - Với các broker trong cụm cluster sẽ luông được chia sẻ các partition khác nhau. Ví dụ như có 1 cluster với 3 broker (B1,B2,B3) và có một topic với 3 partition (P1,P2,P3), và mỗi partition có 3 bản sao thì mỗi partition sẽ được quản lý bởi
      1 broker(ví dụ: B1P1,B2P2,B3P3 <br/>
      => chúng đi cặp với nhau như thế nào là do broker leader quyết định) và chúng sẽ đi cặp với nhau <br/>
      => Khi có 1 topic là "order" có 3 partition trên. Giả sử nếu gửi 1000 message tới topic này thì theo mặc định nếu không 
      gửi key thì nó sẽ chia lần lượt req1 sẽ đi vào P1 và được xử lý bởi B1 và B1 sẽ lưu vào log và khi này B2 và B3 sẽ là broker theo dõi
    - Khi một broker sập thì zookeeper hoặc Kraf sẽ báo cho broker leader và broker leader (gọi là controller) sẽ tìm xem 
    broker nào có dữ liệu bản sao giống với broker bị chết để thay thế và producer và consumer sẽ được chuyển hướng đến broker mới.
- **Broker:** Là một serve máy chủ vật lý hoặc máy ảo, nó là một node trong cluster, Nó có nhiệm vụ xử lý dữ liệu khi producer gửi và nhận với consumer
  - Nó cũng sẽ chịu trách nhiệm quản lý các partition thuộc nó quản lý và đồng bộ dữ liệu ở partition với các broker khác để tránh bị mất dữ liệu
  - Nó cũng chính là cầu nối, serve kết nối consumer và producer
  - Nó cũng thực hiện việc lưu các giá trị ở các partition vào file log segment
- **Producer:** Là ứng dụng hoặc dịch vụ gửi dữ liệu, nó không biết consumer là ai mà chỉ gửi dữ liệu vào topic
- **Consumer:** Là ứng dụng hoặc dịch vụ nhận dữ liệu từ Kafka topic. Nó như kiểu người tiêu thụ dữ liệu
  - Các consumer trong group sẽ liên tục gửi tín hiệu heartbeat để thông báo vẫn consumer vẫn hoạt động đến broker trong
  khoảng thời định kỳ. 
  - Nếu trong khoảng thời gian định kỳ đó consumer không gửi tín hiệu heartbeat thì broker sẽ coi consumer đã chết và
  tiến hành phân phối lại các partition mà consumer đó đang xử lý cho các consumer khác trong nhóm.
- **Group consumer:** Nó như kiểu nhiều consumer hợp tác với nhau để nhận dữ liệu từ một hoặc nhiều topic, Mỗi partition trong topic sẽ được phân bổ xử lý bởi 1 consumer trong group.
- **Topic:** Nó là dạng như kiểu chủ đề nói chuyện. Khi gửi một message thì cần phải xác định nó thuộc topic nào
  - Mỗi topic sẽ có một hoặc nhiều partition tùy vào cách cấu hình. Nhiều partition thì giúp xử lý song song tốt hơn
  - Các partition sẽ được phân bổ vào các broker và khi đẩy message thì nó cũng sẽ đẩy lần lượt theo các partition
- **Partition:** Nó dạng như kiểu các hàng đợi để xử lý dữ liệu cho topic. Một topic có thể cấu hình một hoặc nhiều partition
    - Số lượng partition thì nên được xác định theo nghiệp vụ bởi vì partition giúp xử lý song song nhiều message nên nó sẽ
    giúp việc truyền nhận message được nhanh hơn với lượng message lớn
    - Mỗi partition sẽ chịu sử quản lý bởi 1 broker
    - Các message chứa key được đưa vào partition bằng cách <code>prt = hash(key) chia dư numPartitions</code>. 
    Không có key → Kafka chọn partition theo round-robin để phân tải đều.
    - Khi topic có nhiều partition thì các partition sẽ được chia cho các broker trong cụm cluster để quản lý và backup dữ liệu
    - Ở mỗi broker thì partition chứa message hiện tại là leader và nó sẽ chia sẻ dữ liệu cho các broker flower để broker lưu trữ và backup dữ liệu
    - Cách xác định số lượng partition:

```
  numPartitions = max(expectedThroughput / throughputPerPartition, numConsumers)
```
Trong đó:

- **expectedThroughput**: lưu lượng mong muốn (msg/s).
- **throughputPerPartition**: tốc độ xử lý 1 partition (msg/s).
- **numConsumers**: số consumer tối đa trong cùng group.
    
**Thực tế:**
- Topic nhỏ, ít tải: 1–3 partition.
- Topic xử lý lớn (hàng triệu msg/s): 10–50 partition, đôi khi hơn 100.
- Microservices event bus: thường ~3–12 partition (đủ để scale, nhưng không quá phức tạp).