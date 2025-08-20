using UnityEngine;

public class Invaders : MonoBehaviour
{
    [Header("Invaders")]
    public Invader[] prefabs = new Invader[5];
    public float speed = 1f;
    private Vector3 direction = Vector3.right;
    private Vector3 posicaoInicial;

    [Header("Grid")]
    public int rows = 5;
    public int columns = 11;

    private void Awake()
    {
        posicaoInicial = transform.position;
        CreateInvaderGrid();
    }
    private void Update()
    {

        transform.position += speed * Time.deltaTime * direction;

        Vector3 leftEdge = Camera.main.ViewportToWorldPoint(Vector3.zero);
        Vector3 rightEdge = Camera.main.ViewportToWorldPoint(Vector3.right);

        foreach (Transform invader in transform)
        {
            if (!invader.gameObject.activeInHierarchy)
            {
                continue;
            }
            if (direction == Vector3.right && invader.position.x >= (rightEdge.x - 1f))
            {
                AcancaRow();
                break;
            }
            else if (direction == Vector3.left && invader.position.x <= (leftEdge.x + 1f))
            {
                AcancaRow();
                break;
            }
        }
    }
    private void AcancaRow()
    {
        direction = new Vector3(-direction.x, 0f);
        Vector3 position = transform.position;
        position.y -= 1f;
        transform.position = position;
    }

    private void CreateInvaderGrid()
    {
        for (int i = 0; i < rows; i++)
        {
            float largura = 2f * (columns - 1);
            float altura = 2f * (rows - 1);

            Vector2 centerOffset = new Vector2(-largura * 0.5f, -altura * 0.5f);
            Vector3 rowPosition = new Vector3(centerOffset.x, (2f * i) + centerOffset.y, 0f);

            for (int j = 0; j < columns; j++)
            {
                Invader invader = Instantiate(prefabs[i], transform);
                Vector3 position = rowPosition;
                position.x += 2f * j;
                invader.transform.localPosition = position;
            }
        }
    }
    
}
