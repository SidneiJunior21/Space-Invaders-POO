import pygame
import random
import settings

class Bunker(pygame.sprite.Sprite):
    def __init__(self, x, y, size):
        super().__init__()
        self.hit_points = 5
        
        width, height = int(size[0]), int(size[1])
        self.original_image = pygame.Surface((width, height))
        self.original_image.fill(settings.GREEN)
        
        self.image = self.original_image.copy()
        self.rect = self.image.get_rect(center=(x, y))

    def take_damage(self):
        self.hit_points -= 1
        hole_x = random.randint(5, self.rect.width - 5)
        hole_y = random.randint(5, self.rect.height - 5)
        pygame.draw.circle(self.image, settings.BLACK, (hole_x, hole_y), 4)

        if self.hit_points <= 0:
            self.kill()